package com.vibes.ux.service;

import com.google.gson.Gson;
import com.vibes.ux.domain.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChatService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final Gson gson = new Gson();

    private final List<Topic> topics = new ArrayList<>();

    public List<Topic> getAllTopics() {
        return this.topics;
    }

    public void addTopic(Topic topic) {
        this.topics.add(topic);
        this.kafkaTemplate.send(topic.getId(), gson.toJson(topic));
    }

    public boolean isUniqueTopic(Topic topic) {
        return this.topics.stream().noneMatch(x -> x.getId().equals(topic.getId()));
    }

    public Topic getTopicById(String topicId) {
        return this.topics.stream().filter(x -> x.getId().equals(topicId)).findFirst().get();
    }

    public void sendChatroomToKafka(Topic topic) {
        this.kafkaTemplate.send(topic.getId(), gson.toJson(topic));
    }
}
