package com.socialmedia.app.util;

import com.socialmedia.app.dto.MessageDto;
import com.socialmedia.app.model.Chat;
import com.socialmedia.app.model.Message;
import com.socialmedia.app.model.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Utility class for converting Message entities to MessageDto objects.
 */
@Component
public class MessageConvertor {
    private final ModelMapper modelMapper;

    /**
     * Constructs a MessageConvertor and configures the ModelMapper.
     */
    public MessageConvertor() {
        this.modelMapper = new ModelMapper();

        Converter<User, String> userToUsername =
                ctx -> ctx.getSource().getUsername();

        Converter<Chat, Long> chatToChatId =
                ctx -> ctx.getSource().getId();

        modelMapper.createTypeMap(Message.class, MessageDto.class)
                .addMappings(map -> map
                        .using(userToUsername)
                        .map(Message::getFromUser, MessageDto::setFromUser)
                )
                .addMappings(map -> map
                        .using(chatToChatId)
                        .map(Message::getChat, MessageDto::setChat)
                );
    }

    /**
     * Converts a Message entity to a MessageDto object.
     *
     * @param message the Message entity to convert
     * @return the MessageDto object
     */
    public MessageDto convertToDto(Message message) {
        return modelMapper.map(message, MessageDto.class);
    }
}
