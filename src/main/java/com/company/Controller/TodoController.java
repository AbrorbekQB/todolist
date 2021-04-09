package com.company.Controller;

import com.company.Repository.TodoRepository;
import com.company.dto.CodeMessage;
import com.company.dto.TodoItem;
import com.company.enums.MessageType;
import com.company.enums.TodoItemType;
import com.company.util.InlineKeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.company.enums.MessageType.EDIT;
import static com.company.enums.MessageType.MESSAGE;

public class TodoController {

    private Map<Long, TodoItem> todoItemStep = new HashMap<>();
    private TodoRepository todoRepository = new TodoRepository();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public CodeMessage handle(String command, Long chatId, Integer messageId) {
        CodeMessage codeMessage = new CodeMessage();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        codeMessage.setMessageType(EDIT);

        if (command.startsWith("/todo/")) {
            if (command.endsWith("/list")) {
                StringBuilder stringBuilder = new StringBuilder();

                List<TodoItem> todoItemList = this.todoRepository.getTodoList(chatId);
                if (todoItemList.isEmpty()) {
                    editMessageText.setText("Todo listlar mavjud emas!");
                    List<List<InlineKeyboardButton>> rowCollection = InlineKeyboardUtil.rowsList(
                            InlineKeyboardUtil.rows(InlineKeyboardUtil.button("Go to menu", "menu")));
                    editMessageText.setReplyMarkup(InlineKeyboardUtil.keyboardMarkup(rowCollection));
                    codeMessage.setEditMessageText(editMessageText);
                } else {
                    int counter = 1;
                    for (TodoItem todoItem : todoItemList) {
                        stringBuilder.append("<b>" + counter++ + "</b>\n");
                        stringBuilder.append("Title: " + todoItem.getTitle() + "\n");
                        stringBuilder.append("Content: " + todoItem.getContent() + "\n");
                        stringBuilder.append(simpleDateFormat.format(todoItem.getDate()) + "\n");
                        stringBuilder.append("/todo_edit_" + todoItem.getId() + "\n");
                    }
                    editMessageText.setText(stringBuilder.toString());
                    List<List<InlineKeyboardButton>> rowCollection = InlineKeyboardUtil.rowsList(
                            InlineKeyboardUtil.rows(InlineKeyboardUtil.button("Go to menu", "menu")));

                    editMessageText.setReplyMarkup(InlineKeyboardUtil.keyboardMarkup(rowCollection));
                    editMessageText.setParseMode("HTML");

                }

                codeMessage.setEditMessageText(editMessageText);
                return codeMessage;
            }
            if (command.endsWith("/create")) {
                editMessageText.setText("Title ni jo'nating");
                codeMessage.setEditMessageText(editMessageText);

                TodoItem todoItem = new TodoItem();
                todoItem.setId(messageId.toString());
                todoItem.setUserId(chatId);
                todoItem.setType(TodoItemType.TITLE);

                this.todoItemStep.put(chatId, todoItem);
                return codeMessage;
            }
            String todoId = command.split("/")[command.split("/").length - 1];
            TodoItem todoItem = this.todoRepository.getItem(chatId, todoId);
            if (command.contains("update/title")) {
                todoItem.setType(TodoItemType.UPDATE_TITLE);
                editMessageText.setText("Current Title: " + todoItem.getTitle() + "\nPlease send new title");
                List<List<InlineKeyboardButton>> rowCollection = InlineKeyboardUtil.rowsList(
                        InlineKeyboardUtil.rows(InlineKeyboardUtil.button("Cancel", "/todo/cancel/"+todoId)));
                editMessageText.setReplyMarkup(InlineKeyboardUtil.keyboardMarkup(rowCollection));
                codeMessage.setEditMessageText(editMessageText);
                todoItemStep.put(chatId, todoItem);
                return codeMessage;
            }
            if (command.contains("update/content")) {
                todoItem.setType(TodoItemType.UPDATE_CONTENT);
                editMessageText.setText("Current Content: " + todoItem.getContent() + "\nPlease send new content");
                List<List<InlineKeyboardButton>> rowCollection = InlineKeyboardUtil.rowsList(
                        InlineKeyboardUtil.rows(InlineKeyboardUtil.button("Cancel", "/todo/cancel/"+todoId)));
                editMessageText.setReplyMarkup(InlineKeyboardUtil.keyboardMarkup(rowCollection));
                codeMessage.setEditMessageText(editMessageText);
                todoItemStep.put(chatId, todoItem);
                return codeMessage;
            }
            if (command.contains("/delete")) {
                this.todoRepository.delete(chatId, todoItem);
                todoItemStep.remove(chatId);
                editMessageText.setText("Success");
                List<List<InlineKeyboardButton>> rowCollection = InlineKeyboardUtil.rowsList(
                        InlineKeyboardUtil.rows(InlineKeyboardUtil.button("Go to menu", "menu")));
                editMessageText.setReplyMarkup(InlineKeyboardUtil.keyboardMarkup(rowCollection));
                codeMessage.setEditMessageText(editMessageText);
                return codeMessage;
            }
            if(command.contains("/cancel/")){
                todoItemStep.remove(chatId);
                editMessageText.setText("Canceled!");
                List<List<InlineKeyboardButton>> rowCollection = InlineKeyboardUtil.rowsList(
                        InlineKeyboardUtil.rows(InlineKeyboardUtil.button("ToDo List", "/todo/list", ":zap:")),
                        InlineKeyboardUtil.rows(InlineKeyboardUtil.button("New ToDo", "/todo/create")));
                editMessageText.setReplyMarkup(InlineKeyboardUtil.keyboardMarkup(rowCollection));
                codeMessage.setEditMessageText(editMessageText);
                codeMessage.setMessageType(EDIT);
                todoItem.setType(TodoItemType.FINISHED);
                return codeMessage;
            }
        }

        if (command.startsWith("/todo_")) {
            String todoId = command.split("/todo_edit_")[1];
            TodoItem todoItem = this.todoRepository.getItem(chatId, todoId);
            if (todoItem == null) {
                sendMessage.setText("Topilmadi:(");
                codeMessage.setSendMessage(sendMessage);
                codeMessage.setMessageType(MESSAGE);
                return codeMessage;
            }

            sendMessage.setText("Title: " + todoItem.getTitle() + "\nContent: " + todoItem.getContent() + "\nDate: " + simpleDateFormat.format(todoItem.getDate()));

            sendMessage.setReplyMarkup(getTodoItemKeyBoard(todoId));
            codeMessage.setSendMessage(sendMessage);
            codeMessage.setMessageType(MESSAGE);

        }


        if (this.todoItemStep.containsKey(chatId)) {
            TodoItem todoItem = this.todoItemStep.get(chatId);

            if (todoItem.getType().equals(TodoItemType.TITLE)) {
                todoItem.setTitle(command);
                sendMessage.setText("Title: " + todoItem.getTitle() + "\nSend Content: ");
                todoItem.setType(TodoItemType.CONTENT);

            } else if (todoItem.getType().equals(TodoItemType.CONTENT)) {
                todoItem.setContent(command);

                todoItem.setDate(new Date());
                todoItem.setType(TodoItemType.FINISHED);
                int countTodo = todoRepository.add(chatId, todoItem);
                todoItemStep.remove(chatId);

                List<List<InlineKeyboardButton>> rowCollection = InlineKeyboardUtil.rowsList(
                        InlineKeyboardUtil.rows(InlineKeyboardUtil.button("ToDo List", "/todo/list", ":zap:")),
                        InlineKeyboardUtil.rows(InlineKeyboardUtil.button("New ToDo", "/todo/create")));

                sendMessage.setReplyMarkup(InlineKeyboardUtil.keyboardMarkup(rowCollection));

                sendMessage.setText("TodoItem: " + countTodo + "\nTitle: " + todoItem.getTitle() + "\n" + "Content: " + todoItem.getContent());
            } else if (todoItem.getType().equals(TodoItemType.UPDATE_TITLE)) {
                todoItem.setTitle(command);
                this.todoItemStep.remove(chatId);

                todoItem.setType(TodoItemType.FINISHED);

                sendMessage.setText("Title: " + todoItem.getTitle() + "\nContent: " + todoItem.getContent() + "\nDate: " + simpleDateFormat.format(todoItem.getDate()));

                sendMessage.setReplyMarkup(getTodoItemKeyBoard(todoItem.getId()));
            } else if(todoItem.getType().equals(TodoItemType.UPDATE_CONTENT)){
                todoItem.setContent(command);
                this.todoItemStep.remove(chatId);

                todoItem.setType(TodoItemType.FINISHED);

                sendMessage.setText("Title: " + todoItem.getTitle() + "\nContent: " + todoItem.getContent() + "\nDate: " + simpleDateFormat.format(todoItem.getDate()));

                sendMessage.setReplyMarkup(getTodoItemKeyBoard(todoItem.getId()));
            }
            codeMessage.setSendMessage(sendMessage);
            codeMessage.setMessageType(MESSAGE);
        }
        codeMessage.setEditMessageText(editMessageText);
        return codeMessage;
    }

    public Map<Long, TodoItem> getTodoItemStep() {
        return todoItemStep;
    }

    private InlineKeyboardMarkup getTodoItemKeyBoard(String id){
        List<List<InlineKeyboardButton>> rowCollection = InlineKeyboardUtil.rowsList(
                InlineKeyboardUtil.rows(InlineKeyboardUtil.button("Update Title", "/todo/update/title/" + id, ":zap:"),
                        InlineKeyboardUtil.button("Update Content", "/todo/update/content/" + id, ":zap:"),
                        InlineKeyboardUtil.button("Delete", "/todo/delete/" + id, ":zap:")),
                InlineKeyboardUtil.rows(InlineKeyboardUtil.button("Go to menu", "menu")));

        return InlineKeyboardUtil.keyboardMarkup(rowCollection);
    }

}