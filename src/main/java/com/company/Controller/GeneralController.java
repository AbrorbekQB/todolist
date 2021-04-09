package com.company.Controller;

import com.company.dto.CodeMessage;
import com.company.util.InlineKeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.company.enums.MessageType.EDIT;
import static com.company.enums.MessageType.MESSAGE;
import static com.company.enums.MessageType.MESSAGE_VIDEO;

public class GeneralController {
    public CodeMessage handle(String text, Long chatId, Integer messageId) {
        SendMessage sendMessage = new SendMessage();
        CodeMessage codeMessage = new CodeMessage();
        codeMessage.setMessageType(MESSAGE);
        sendMessage.setChatId(chatId);

        if (text.equals("/start")) {
            sendMessage.setText("<b>TODO</b> botiga hush kelibsiz");
            sendMessage.setParseMode("HTML");

            List<List<InlineKeyboardButton>> rowCollection = InlineKeyboardUtil.rowsList(
                    InlineKeyboardUtil.rows(InlineKeyboardUtil.button("Go to menu", "menu")));

            sendMessage.setReplyMarkup(InlineKeyboardUtil.keyboardMarkup(rowCollection));
            codeMessage.setSendMessage(sendMessage);
        } else if (text.equals("/help")) {
            sendMessage.setText("Yordam olish uchun [havola](https://www.youtube.com/watch?v=0VkLPkIu10c&list=PLeUA5nZ_B5228g6VF3eY7Pc1N4dwq5W5K&index=9)");
            sendMessage.setParseMode("Markdown");
            sendMessage.disableWebPagePreview();

            SendVideo sendVideo = new SendVideo();
            sendVideo.setChatId(chatId);
            sendVideo.setVideo("BAACAgIAAxkBAAO5YGBsknJtmL0QZvxCg9FlDvPGHVoAAgINAAI2CwlLqOo4T7L10KQeBA");
            sendVideo.setCaption("Bu video siz uchun muhum");

            codeMessage.setSendVideo(sendVideo);
            codeMessage.setSendMessage(sendMessage);
            codeMessage.setMessageType(MESSAGE_VIDEO);
        } else if (text.equals("/setting")) {
            sendMessage.setText("Setting hali to'ldirilmagan");

            codeMessage.setSendMessage(sendMessage);
        } else if(text.equals("menu")){
            EditMessageText editMessageText = new EditMessageText();

            editMessageText.setText("Asosiy menu");
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);

            List<List<InlineKeyboardButton>> rowCollection = InlineKeyboardUtil.rowsList(
                    InlineKeyboardUtil.rows(InlineKeyboardUtil.button("ToDo List", "/todo/list",":zap:")),
                    InlineKeyboardUtil.rows(InlineKeyboardUtil.button("New ToDo", "/todo/create")));

            editMessageText.setReplyMarkup(InlineKeyboardUtil.keyboardMarkup(rowCollection));

            codeMessage.setEditMessageText(editMessageText);
            codeMessage.setMessageType(EDIT);
        }

        return codeMessage;
    }
}
