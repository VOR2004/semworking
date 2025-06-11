package ru.itis.semworkapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.semworkapp.entities.ChatEntity;
import ru.itis.semworkapp.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    private UUID id;
    private ChatEntity chat;
    private UserEntity sender;
    private String content;
    private LocalDateTime sentAt;
}
