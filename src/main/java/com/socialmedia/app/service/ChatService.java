package com.socialmedia.app.service;

import com.socialmedia.app.dto.ChatDto;
import com.socialmedia.app.model.Chat;
import com.socialmedia.app.model.Message;
import com.socialmedia.app.repository.ChatRepository;
import com.socialmedia.app.repository.MessageRepository;
import com.socialmedia.app.repository.UserRepository;
import com.socialmedia.app.util.ChatConvertor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

/**
 * Service class for managing chat functionality.
 */
@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatConvertor chatConvertor;

    public ChatService(ChatRepository chatRepository, MessageRepository messageRepository, UserRepository userRepository, ChatConvertor chatConvertor) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.chatConvertor = chatConvertor;
    }

    /**
     * Retrieves the chat with a specific user.
     *
     * @param username  the username of the other user in the chat
     * @param principal the authenticated user principal
     * @return the chat DTO
     * @throws ResponseStatusException if the chat is not found
     */
    public ChatDto getChatWithUser(String username, Principal principal) {
        var chat = chatRepository
                .findByUser1_UsernameAndUser2_Username(principal.getName(), username)
                .or(() -> chatRepository.findByUser1_UsernameAndUser2_Username(username, principal.getName()));

        if(chat.isPresent()) {
            return chatConvertor.convertToDto(chat.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat with given user not found!");
        }
    }

    /**
     * Sends a message to a specific user.
     *
     * @param username  the username of the recipient user
     * @param text      the message text
     * @param principal the authenticated user principal
     * @throws ResponseStatusException if the recipient user or chat is not found, or if the recipient is not a friend
     */
    public void sendMessageToUser(String username, String text, Principal principal) {
        var userFrom = userRepository
                .findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found!"));
        var userTo = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found!"));

        if (!userFrom.searchFriend(userTo)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "To send a message, the user must be your friend!");
        }

        var chat = chatRepository
                .findByUser1_UsernameAndUser2_Username(principal.getName(), username)
                .or(() -> chatRepository.findByUser1_UsernameAndUser2_Username(username, principal.getName()));

        if (chat.isEmpty()) {
            var newChat = new Chat(userFrom, userTo);
            chatRepository.save(newChat);

            var message = new Message(text, userFrom, newChat);
            messageRepository.save(message);
        } else {
            var message = new Message(text, userFrom, chat.get());
            messageRepository.save(message);
        }
    }
}
