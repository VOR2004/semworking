package ru.itis.semworkapp.controller.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itis.semworkapp.entities.ChatEntity;
import ru.itis.semworkapp.entities.UserEntity;
import ru.itis.semworkapp.forms.MessageForm;
import ru.itis.semworkapp.service.chat.ChatService;
import ru.itis.semworkapp.service.user.UserService;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;

    @PostMapping("/chat/start/{sellerId}")
    public String startChat(
            @PathVariable UUID sellerId,
            @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity buyer = userService.requireUserByEmail(userDetails.getUsername());
        UserEntity seller = userService.requireUserById(sellerId);
        ChatEntity chat = chatService.getOrCreateChat(seller, buyer);
        log.info(ChatLogMessages.CHAT_STARTED);
        return "redirect:/chat/" + chat.getId();
    }

    @GetMapping("/chat/{chatId}")
    public String viewChat(
            @PathVariable UUID chatId, Model model,
            @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.requireUserByEmail(userDetails.getUsername());
        ChatEntity chat = chatService.getChatIfUserHasAccess(chatId, user);
        String sellerAvatar = chat.getSeller().getProfile().getAvatarUrl();
        String buyerAvatar = chat.getBuyer().getProfile().getAvatarUrl();
        model.addAttribute("chat", chat);
        model.addAttribute("messages", chatService.getMessages(chat));
        model.addAttribute("messageForm", new MessageForm());
        model.addAttribute("currentUser", user);
        model.addAttribute("sellerAvatar", sellerAvatar);
        model.addAttribute("buyerAvatar", buyerAvatar);
        return "chat/view";
    }

    @PostMapping("/chat/{chatId}")
    public String sendMessage(
            @PathVariable UUID chatId,
            @ModelAttribute MessageForm messageForm,
            @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity sender = userService.requireUserByEmail(userDetails.getUsername());
        ChatEntity chat = chatService.getChatIfUserHasAccess(chatId, sender);
        chatService.sendMessage(chat, sender, messageForm.getContent());
        return "redirect:/chat/" + chat.getId();
    }


    @GetMapping("/chats")
    public String listChats(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        UserEntity user = userService.requireUserByEmail(userDetails.getUsername());
        model.addAttribute("chats", chatService.getChatsForUser(user));
        model.addAttribute("currentUserEmail", user.getEmail());
        return "chat/list";
    }
}

