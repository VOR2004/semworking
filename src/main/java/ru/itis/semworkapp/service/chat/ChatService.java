package ru.itis.semworkapp.service.chat;

import ru.itis.semworkapp.entities.ChatEntity;
import ru.itis.semworkapp.entities.MessageEntity;
import ru.itis.semworkapp.entities.UserEntity;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    ChatEntity getOrCreateChat(UserEntity seller, UserEntity buyer);
    List<MessageEntity> getMessages(ChatEntity chat);
    MessageEntity sendMessage(ChatEntity chat, UserEntity sender, String content);

    ChatEntity getChatIfUserHasAccess(UUID chatId, UserEntity user);

    List<ChatEntity> getChatsForUser(UserEntity user);
}