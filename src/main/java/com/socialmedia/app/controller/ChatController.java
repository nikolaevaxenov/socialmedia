package com.socialmedia.app.controller;

import com.socialmedia.app.dto.ChatDto;
import com.socialmedia.app.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ChatDto getChatWithUser(@PathVariable String username, Principal principal) {
        return chatService.getChatWithUser(username, principal);
    }

    @PostMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void sendMessageToUser(@PathVariable String username, @RequestBody String text, Principal principal) {
        chatService.sendMessageToUser(username, text, principal);
    }
}
