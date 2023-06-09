package com.socialmedia.app.util;

import com.socialmedia.app.dto.ChatDto;
import com.socialmedia.app.dto.MessageDto;
import com.socialmedia.app.model.Chat;
import com.socialmedia.app.model.Message;
import com.socialmedia.app.model.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class for converting Chat entities to ChatDto objects.
 */
@Component
public class ChatConvertor {
    private final ModelMapper modelMapper;
    private final MessageConvertor messageConvertor;

    /**
     * Constructs a ChatConvertor with the provided MessageConvertor.
     *
     * @param messageConvertor the convertor for converting Message entities to MessageDto objects
     */
    public ChatConvertor(MessageConvertor messageConvertor) {
        this.messageConvertor = messageConvertor;
        this.modelMapper = new ModelMapper();


        Converter<User, String> userToUsername =
                ctx -> ctx.getSource().getUsername();

        Converter<Set<Message>, Set<MessageDto>> messagesToMessagesDto =
                ctx -> ctx.getSource()
                        .stream()
                        .map(messageConvertor::convertToDto)
                        .collect(Collectors.toSet());

        modelMapper.createTypeMap(Chat.class, ChatDto.class)
                .addMappings(map -> map
                        .using(userToUsername)
                        .map(Chat::getUser1, ChatDto::setUser1)
                )
                .addMappings(map -> map
                        .using(userToUsername)
                        .map(Chat::getUser2, ChatDto::setUser2)
                )
                .addMappings(map -> map
                        .using(messagesToMessagesDto)
                        .map(Chat::getMessage, ChatDto::setMessage)
                );
    }

    /**
     * Converts a Chat entity to a ChatDto object.
     *
     * @param chat the Chat entity to convert
     * @return the ChatDto object
     */
    public ChatDto convertToDto(Chat chat) {
        return modelMapper.map(chat, ChatDto.class);
    }
}
