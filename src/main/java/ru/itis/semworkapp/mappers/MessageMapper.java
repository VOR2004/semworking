package ru.itis.semworkapp.mappers;

import org.springframework.stereotype.Component;
import ru.itis.semworkapp.dto.MessageDto;
import ru.itis.semworkapp.entities.MessageEntity;

@Component
public class MessageMapper {
    public MessageDto toDto(MessageEntity entity) {
        return MessageDto.builder()
                .id(entity.getId())
                .sender(entity.getSender())
                .content(entity.getContent())
                .sentAt(entity.getSentAt())
                .chat(entity.getChat())
                .build();
    }
}
