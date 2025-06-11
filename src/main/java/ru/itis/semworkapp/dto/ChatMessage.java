package ru.itis.semworkapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChatMessage {
    private UUID chatId;
    private UUID senderId;
    private String senderName;
    private String content;
    private String senderAvatar;
}
