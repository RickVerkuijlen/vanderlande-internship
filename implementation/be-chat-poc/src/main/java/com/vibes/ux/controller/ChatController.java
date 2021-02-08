package com.vibes.ux.controller;

import com.google.gson.Gson;
import com.vibes.ux.domain.ChatMessage;
import com.vibes.ux.domain.Topic;
import com.vibes.ux.domain.User;
import com.vibes.ux.messages.WsMessage;
import com.vibes.ux.messages.MessageType;
import com.vibes.ux.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;

@Controller
public class ChatController {
    private final ChatService chatService;

    private Gson gson;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
        gson = new Gson();
    }

    @MessageMapping("/createTopic")
    @SendTo("/topics")
    public WsMessage createTopic(@Payload WsMessage wsMessage) {
        Topic topic = gson.fromJson(wsMessage.getContent(), Topic.class);

        if(this.chatService.isUniqueTopic(topic)) {
            this.chatService.addTopic(topic);

            return WsMessage.builder()
                    .content(gson.toJson(this.chatService.getAllTopics()))
                    .messageType(MessageType.INFO)
                    .build();
        } else {
            return WsMessage.builder()
                    .content("Topic already exists")
                    .messageType(MessageType.ERROR)
                    .build();
        }
    }

    @MessageMapping("/allTopics")
    @SendTo("/topics")
    public WsMessage sendAllTopics() {
        return WsMessage.builder()
                .content(gson.toJson(this.chatService.getAllTopics()))
                .messageType(MessageType.INFO)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    @MessageMapping("/{topicId}/sendMessage")
    @SendTo("/topics/{topicId}")
    public WsMessage sendMessage(@DestinationVariable String topicId, @Payload WsMessage wsMessage) {
        Topic currentTopic = this.chatService.getTopicById(topicId);

        ChatMessage chatMessage = gson.fromJson(wsMessage.getContent(), ChatMessage.class);

        currentTopic.addMessage(chatMessage);

        this.chatService.sendChatroomToKafka(currentTopic);

        return WsMessage.builder()
                .content(gson.toJson(currentTopic))
                .messageType(MessageType.CHAT)
                .build();
    }

    @MessageMapping("/{topicId}/addUser")
    @SendTo("/topics/{topicId}")
    public WsMessage addUser(@DestinationVariable String topicId, @Payload WsMessage wsMessage) {
        Topic currentTopic = this.chatService.getTopicById(topicId);

        User user = gson.fromJson(wsMessage.getContent(), User.class);

        currentTopic.addUser(user);

        this.chatService.sendChatroomToKafka(currentTopic);

        return WsMessage.builder()
                .content(gson.toJson(currentTopic))
                .messageType(MessageType.INFO)
                .build();
    }
}
