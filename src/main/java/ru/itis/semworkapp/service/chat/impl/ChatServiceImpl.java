package ru.itis.semworkapp.service.chat.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.semworkapp.dto.MessageDto;
import ru.itis.semworkapp.entities.ChatEntity;
import ru.itis.semworkapp.entities.MessageEntity;
import ru.itis.semworkapp.entities.UserEntity;
import ru.itis.semworkapp.exceptions.chat.ChatAccessDeniedException;
import ru.itis.semworkapp.exceptions.chat.ChatNotFoundException;
import ru.itis.semworkapp.mappers.MessageMapper;
import ru.itis.semworkapp.repositories.chat.ChatRepository;
import ru.itis.semworkapp.repositories.chat.MessageRepository;
import ru.itis.semworkapp.service.chat.ChatService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    public ChatEntity getOrCreateChat(UserEntity seller, UserEntity buyer) {
        return chatRepository.findBySellerAndBuyer(seller, buyer)
                .orElseGet(() -> chatRepository.save(ChatEntity.builder()
                        .seller(seller)
                        .buyer(buyer)
                        .build()));
    }

    @Override
    public List<MessageDto> getMessages(ChatEntity chat) {
        return messageRepository.findByChatOrderBySentAtAsc(chat)
                .stream().map(messageMapper::toDto).toList();
    }

    @Override
    public MessageEntity sendMessage(ChatEntity chat, UserEntity sender, String content) {
        MessageEntity message = MessageEntity.builder()
                .chat(chat)
                .sender(sender)
                .sentAt(LocalDateTime.now())
                .content(content)
                .build();
        return messageRepository.save(message);
    }

    @Override
    public ChatEntity getChatIfUserHasAccess(UUID chatId, UserEntity user) {
        ChatEntity chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));
        if (!chat.getSeller().getId().equals(user.getId()) &&
                !chat.getBuyer().getId().equals(user.getId())) {
            throw new ChatAccessDeniedException(chatId);
        }
        return chat;
    }

    @Override
    public List<ChatEntity> getChatsForUser(UserEntity user) {
        return chatRepository.findAllBySellerOrBuyer(user, user);
    }
}
