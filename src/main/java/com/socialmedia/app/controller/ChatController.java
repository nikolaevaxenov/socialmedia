package com.socialmedia.app.controller;

import com.socialmedia.app.dto.ChatDto;
import com.socialmedia.app.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Controller class for handling chat-related endpoints.
 */
@RestController
@RequestMapping("/api/v1/chat")
@Tag(name = "Chat", description = "Endpoints for handling chat-related operations")
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
    @Operation(summary = "Get Chat with User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ChatDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ChatDto getChatWithUser(@Parameter(description = "The username of the user to retrieve the chat with.", required = true) @PathVariable String username,
                                   Principal principal) {
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
    @Operation(summary = "Send Message to User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public void sendMessageToUser(@Parameter(description = "The username of the user to send the message to.", required = true) @PathVariable String username,
                                  @RequestBody String text,
                                  Principal principal) {
        chatService.sendMessageToUser(username, text, principal);
    }
}
