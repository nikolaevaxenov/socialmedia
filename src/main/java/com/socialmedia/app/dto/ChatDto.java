package com.socialmedia.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ChatDto {
    private Long id;
    private String user1;
    private String user2;
    private Set<MessageDto> message;

    public ChatDto(Long id, String user1, String user2) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatDto chatDto)) return false;
        return Objects.equals(getId(), chatDto.getId()) && Objects.equals(getUser1(), chatDto.getUser1()) && Objects.equals(getUser2(), chatDto.getUser2());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser1(), getUser2());
    }
}
