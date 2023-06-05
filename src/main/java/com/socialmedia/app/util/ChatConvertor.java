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

@Component
public class ChatConvertor {
    private final ModelMapper modelMapper;
    private final MessageConvertor messageConvertor;

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

    public ChatDto convertToDto(Chat chat) {
        return modelMapper.map(chat, ChatDto.class);
    }
}
