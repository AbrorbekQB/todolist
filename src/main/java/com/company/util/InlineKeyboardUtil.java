package com.company.util;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InlineKeyboardUtil {
    public static InlineKeyboardButton button(String text, String callBackData){
        return new InlineKeyboardButton().setText(text).setCallbackData(callBackData);
    }
    public static InlineKeyboardButton button(String text, String callBackData, String emoji){
        String emojiText = EmojiParser.parseToUnicode(emoji+" "+text+" " +emoji);
        return new InlineKeyboardButton().setText(emojiText).setCallbackData(callBackData);
    }

    public static List<InlineKeyboardButton> rows (InlineKeyboardButton... inlineKeyboardButtons){
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.addAll(Arrays.asList(inlineKeyboardButtons));
        return row;
    }

    public static List<List<InlineKeyboardButton>> rowsList (List<InlineKeyboardButton>... lists){
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        list.addAll(Arrays.asList(lists));
        return list;
    }

    public static InlineKeyboardMarkup keyboardMarkup(List<List<InlineKeyboardButton>> rowsList){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsList);
        return inlineKeyboardMarkup;
    }
}
