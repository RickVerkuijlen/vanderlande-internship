package com.vibes.ux.kafka;

import com.google.gson.Gson;
import com.vibes.ux.domain.Topic;
import com.vibes.ux.messages.MessageType;
import com.vibes.ux.messages.WsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    @Autowired
    SimpMessagingTemplate template;

    private Gson gson = new Gson();

    @KafkaListener(
            topics = {"Conveyor-1", "Conveyor-2"},
            groupId = "vibes-ux-chat"
    )

    public void listen(String topic) {
        System.out.println("Sending via kafka listener..");
        System.out.println(topic);
        WsMessage message = WsMessage.builder().messageType(MessageType.CHAT).content(topic).build();
        template.convertAndSend("/vibes-chat/topics", message);
    }
}
