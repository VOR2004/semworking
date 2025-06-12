package ru.itis.semworkapp.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.itis.semworkapp.dto.ChatMessage;
import ru.itis.semworkapp.entities.ChatEntity;
import ru.itis.semworkapp.entities.UserEntity;
import ru.itis.semworkapp.exceptions.ChatInvalidIdException;
import ru.itis.semworkapp.service.chat.ChatService;
import ru.itis.semworkapp.service.user.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatService chatService;
    private final UserService userService;
    private final Map<UUID, List<WebSocketSession>> chatSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        UUID chatId = getChatId(session);
        chatSessions.computeIfAbsent(chatId, k -> new ArrayList<>()).add(session);
    }

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        ChatMessage msg = objectMapper.readValue(message.getPayload(), ChatMessage.class);

        UserEntity sender = userService.requireUserById(msg.getSenderId());
        ChatEntity chat = chatService.getChatIfUserHasAccess(msg.getChatId(), sender);

        chatService.sendMessage(chat, sender, msg.getContent());

        String avatarUrl = null;
        if (sender.getId().equals(chat.getSeller().getId())) {
            avatarUrl = chat.getSeller().getProfile() != null ? chat.getSeller().getProfile().getAvatarUrl() : null;
        } else if (sender.getId().equals(chat.getBuyer().getId())) {
            avatarUrl = chat.getBuyer().getProfile() != null ? chat.getBuyer().getProfile().getAvatarUrl() : null;
        }
        msg.setAvatarUrl(avatarUrl != null ? avatarUrl : "/images/avatar-placeholder.png");
        msg.setSentAt(LocalDateTime.now().toString());

        List<WebSocketSession> sessions = chatSessions.getOrDefault(msg.getChatId(), Collections.emptyList());
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
            }
        }
    }


    private UUID getChatId(WebSocketSession session) {
        String query = session.getUri().getQuery();
        for (String param : query.split("&")) {
            String[] kv = param.split("=");
            if (kv.length == 2 && kv[0].equals("chatId")) {
                return UUID.fromString(kv[1]);
            }
        }
        throw new ChatInvalidIdException();
    }
}
