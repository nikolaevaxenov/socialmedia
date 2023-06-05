package com.socialmedia.app.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "friend_status")
public class FriendStatus {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User userFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    private User userTo;

    public FriendStatus(User userFrom, User userTo) {
        this.userFrom = userFrom;
        this.userTo = userTo;
    }

    @Override
    public String toString() {
        return "FriendStatus{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendStatus that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
