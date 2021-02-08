package service;

import com.vibes.ux.domain.Topic;
import com.vibes.ux.service.ChatService;
import mock.KafkaMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @Disabled
    @BeforeEach
    public void init() {
        chatService = new ChatService();
        Topic topic = Topic.builder()
                .id("Testcase")
                .build();
        this.chatService.addTopic(topic);
    }

    @Disabled
    @Test
    public void addTopic_success() {
        Topic topic = Topic.builder()
                .id("new Testcase")
                .build();
        this.chatService.addTopic(topic);

        assertNotNull(this.chatService.getAllTopics());
    }

    @Disabled
    @Test
    public void getAllTopics_2_true() {
        Topic topic = Topic.builder()
                .id("new Testcase")
                .build();
        this.chatService.addTopic(topic);

        int actual = this.chatService.getAllTopics().size();
        int expected = 2;

        assertEquals(expected, actual);
    }

    @Disabled
    @Test
    public void addSimilarTopic_returnFalse() {
        Topic topic = Topic.builder()
                .id("Testcase")
                .build();

        assertFalse(this.chatService.isUniqueTopic(topic));
    }
}
