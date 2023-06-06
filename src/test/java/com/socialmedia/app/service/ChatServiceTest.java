package com.socialmedia.app.service;

import com.socialmedia.app.dto.ChatDto;
import com.socialmedia.app.model.Chat;
import com.socialmedia.app.model.Message;
import com.socialmedia.app.model.User;
import com.socialmedia.app.repository.ChatRepository;
import com.socialmedia.app.repository.MessageRepository;
import com.socialmedia.app.repository.UserRepository;
import com.socialmedia.app.util.ChatConvertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChatConvertor chatConvertor;
    @Mock
    private Principal principal;

    private ChatService chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatService = new ChatService(chatRepository, messageRepository, userRepository, chatConvertor);
    }

    @Test
    void getChatWithUser_ChatExists_ReturnsChatDto() {
        // Arrange
        String username = "otherUser";
        Chat chat = new Chat();
        ChatDto chatDto = new ChatDto();
        when(chatRepository.findByUser1_UsernameAndUser2_Username(principal.getName(), username)).thenReturn(Optional.of(chat));
        when(chatConvertor.convertToDto(chat)).thenReturn(chatDto);

        // Act
        ChatDto result = chatService.getChatWithUser(username, principal);

        // Assert
        assertEquals(chatDto, result);
        verify(chatRepository).findByUser1_UsernameAndUser2_Username(principal.getName(), username);
        verify(chatConvertor).convertToDto(chat);
    }

    @Test
    void getChatWithUser_ChatNotExists_ThrowsException() {
        // Arrange
        String username = "otherUser";
        when(chatRepository.findByUser1_UsernameAndUser2_Username(principal.getName(), username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> chatService.getChatWithUser(username, principal));
        verify(chatRepository).findByUser1_UsernameAndUser2_Username(principal.getName(), username);
        verifyNoMoreInteractions(chatConvertor);
    }

    @Test
    void sendMessageToUser_ValidParameters_CreatesMessage() {
        // Arrange
        String username = "recipientUser";
        String text = "Hello!";
        User userFrom = mock(User.class);
        User userTo = mock(User.class);
        Chat chat = new Chat();
        when(userRepository.findByUsername(principal.getName())).thenReturn(Optional.of(userFrom));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userTo));
        when(userFrom.searchFriend(userTo)).thenReturn(true);
        when(chatRepository.findByUser1_UsernameAndUser2_Username(principal.getName(), username)).thenReturn(Optional.of(chat));

        // Act
        chatService.sendMessageToUser(username, text, principal);

        // Assert
        verify(userRepository).findByUsername(principal.getName());
        verify(userRepository).findByUsername(username);
        verify(userFrom).searchFriend(userTo);
        verify(chatRepository).findByUser1_UsernameAndUser2_Username(principal.getName(), username);
        verify(messageRepository).save(new Message(text, userFrom, chat));
        verifyNoMoreInteractions(chatRepository);
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(userFrom);
        verifyNoMoreInteractions(userTo);
    }

    @Test
    void sendMessageToUser_UserNotFound_ThrowsException() {
        // Arrange
        String username = "recipientUser";
        String text = "Hello!";
        when(userRepository.findByUsername(principal.getName())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> chatService.sendMessageToUser(username, text, principal));
        verify(userRepository).findByUsername(principal.getName());
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(chatRepository);
        verifyNoMoreInteractions(messageRepository);
    }

    @Test
    void sendMessageToUser_UserNotFriend_ThrowsException() {
        // Arrange
        String username = "recipientUser";
        String text = "Hello!";
        User userFrom = mock(User.class);
        User userTo = mock(User.class);
        when(userRepository.findByUsername(principal.getName())).thenReturn(Optional.of(userFrom));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userTo));
        when(userFrom.searchFriend(userTo)).thenReturn(false);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> chatService.sendMessageToUser(username, text, principal));
        verify(userRepository).findByUsername(principal.getName());
        verify(userRepository).findByUsername(username);
        verify(userFrom).searchFriend(userTo);
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(chatRepository);
        verifyNoMoreInteractions(messageRepository);
    }
}