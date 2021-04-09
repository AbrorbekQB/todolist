package com.company.Controller;

import com.company.Main;
import com.company.dto.CodeMessage;
import com.company.service.FileInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MenuController extends TelegramLongPollingBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private GeneralController generalController;
    private FileInfoService fileInfoService;
    private TodoController todoController;

    public MenuController() {
        this.generalController = new GeneralController();
        this.fileInfoService = new FileInfoService();
        this.todoController = new TodoController();
    }

    public String getBotUsername() {
        return "@abrorqb_bot";
    }

    public String getBotToken() {
        return "1719025909:AAHtbqB5Sz1HJZWgaBbfWOE7U2NjZyINyvo";
    }

    public void onUpdateReceived(Update update) {

        CodeMessage codeMessage;
        Message message = update.getMessage();

        try {

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();

            String data = callbackQuery.getData();
            message = callbackQuery.getMessage();
            LOGGER.info(message.toString());

            if(data.startsWith("/todo")){
                codeMessage = todoController.handle(data, message.getChatId(), message.getMessageId());
            } else {
                codeMessage = generalController.handle(data, message.getChatId(), message.getMessageId());
            }

            this.sendMsg(codeMessage);
        } else if (message != null) {

            LOGGER.info(message.toString());
            String text = message.getText();
            if (text != null) {
                if (text.equals("/start") || text.equals("/help") || text.equals("/setting")) {
                    codeMessage = generalController.handle(text, message.getChatId(), message.getMessageId());
                    this.sendMsg(codeMessage);
                } else if (this.todoController.getTodoItemStep().containsKey(message.getChatId()) || text.startsWith("/todo_")) {
                    this.sendMsg(this.todoController.handle(text, message.getChatId(), message.getMessageId()));
                } else {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(message.getChatId());
                    sendMessage.setText("Mavjud emas");
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            } else {
//            FILE
                this.sendMsg(this.fileInfoService.getFileInfo(message));

            }
        }
        } catch (Exception e){
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendMsg(CodeMessage codeMessage) {
        try {
            switch (codeMessage.getMessageType()) {
                case EDIT:
                    execute(codeMessage.getEditMessageText());
                    break;
                case MESSAGE:
                    execute(codeMessage.getSendMessage());
                    break;
                case MESSAGE_VIDEO:
                    execute(codeMessage.getSendVideo());
                    execute(codeMessage.getSendMessage());
                    break;
                default:
                    break;
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
