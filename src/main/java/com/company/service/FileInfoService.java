package com.company.service;

import com.company.dto.CodeMessage;
import com.company.enums.MessageType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Video;

import java.util.List;

public class FileInfoService {
    public CodeMessage getFileInfo(Message message){
        CodeMessage codeMessage = new CodeMessage();
        codeMessage.setMessageType(MessageType.MESSAGE);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());

        if(message.getPhoto() != null){
            String s = show_photo_detail(message.getPhoto());
            sendMessage.setText(s);
        } else if(message.getVideo() != null){
            String s = show_video_detail(message.getVideo());
            sendMessage.setText(s);
        } else {
            sendMessage.setText("NOT FOUND");
        }

        codeMessage.setSendMessage(sendMessage);
        return codeMessage;
    }

    private String show_photo_detail(List<PhotoSize> photoSizeList){
        String s = "------------------------------------------PHOTO INFO-------------------------------------\n";
        for (PhotoSize photoSize :photoSizeList) {
            s+=" Size: "+photoSize.getFileSize()+"            ID = "+photoSize.getFileId()+"\n";
        }
        return s;
    }
    private String show_video_detail(Video video){
        String s = "------------------------------------------VIDEO INFO-------------------------------------\n";
        s += video.toString();
        return s;
    }
}
