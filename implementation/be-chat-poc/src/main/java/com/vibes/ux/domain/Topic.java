package com.vibes.ux.domain;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Data
@Builder
public class Topic {
    private String id;

    @Setter(AccessLevel.NONE)
    private List<User> joinedUsers;

    @Setter(AccessLevel.NONE)
    private List<ChatMessage> chatMessages;

    public void addUser(User user) {
        if(isUniqueUsername(user)) this.joinedUsers.add(user);
    }

    public void addMessage(ChatMessage message) {
        this.chatMessages.add(message);
    }

    private boolean isUniqueUsername(User user) {
        return this.joinedUsers.stream().noneMatch(x -> x.getName().equals(user.getName()));
    }
}
