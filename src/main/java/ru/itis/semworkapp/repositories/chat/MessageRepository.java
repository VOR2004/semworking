package ru.itis.semworkapp.repositories.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.semworkapp.entities.ChatEntity;
import ru.itis.semworkapp.entities.MessageEntity;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {
    List<MessageEntity> findByChatOrderBySentAtAsc(ChatEntity chat);
}