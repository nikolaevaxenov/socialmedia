package com.socialmedia.app.controller;

import com.socialmedia.app.dto.ChatDto;
import com.socialmedia.app.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Controller class for handling chat-related endpoints.
 */
@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;

    /**
     * Constructs the ChatController class.
     *
     * @param chatService the ChatService implementation.
     */
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Retrieves the chat with the specified user.
     *
     * @param username  the username of the user to retrieve the chat with.
     * @param principal the authenticated principal user.
     * @return the ChatDto object representing the chat.
     */
    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ChatDto getChatWithUser(@PathVariable String username, Principal principal) {
        return chatService.getChatWithUser(username, principal);
    }

    /**
     * Sends a message to the specified user.
     *
     * @param username  the username of the user to send the message to.
     * @param text      the text content of the message.
     * @param principal the authenticated principal user.
     */
    @PostMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void sendMessageToUser(@PathVariable String username, @RequestBody String text, Principal principal) {
        chatService.sendMessageToUser(username, text, principal);
    }
}
