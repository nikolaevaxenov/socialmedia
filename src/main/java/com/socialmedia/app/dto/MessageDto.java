package com.socialmedia.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class MessageDto {
    private Long id;
    private String text;
    private String fromUser;
    private Long chat;
    private Instant createdAt;

    public MessageDto(String text, String fromUser, Long chat) {
        this.text = text;
        this.fromUser = fromUser;
        this.chat = chat;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", fromUser='" + fromUser + '\'' +
                ", chat=" + chat +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageDto that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getText(), that.getText()) && Objects.equals(getFromUser(), that.getFromUser()) && Objects.equals(getChat(), that.getChat()) && Objects.equals(getCreatedAt(), that.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText(), getFromUser(), getChat(), getCreatedAt());
    }
}
