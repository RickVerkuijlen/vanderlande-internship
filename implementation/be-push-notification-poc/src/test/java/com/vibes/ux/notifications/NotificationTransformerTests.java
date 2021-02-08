package com.vibes.ux.notifications;

import com.vibes.ux.notifications.domain.Notification;
import com.vibes.ux.notifications.domain.NotificationAction;
import com.vibes.ux.notifications.transformer.NotificationTransformer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Not;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class NotificationTransformerTests {

    private NotificationTransformer notificationFactory;

    @Before
    public void setUp() {
        notificationFactory = new NotificationTransformer();
    }

    @Test
    public void Factory_WithBody_true() {
        Notification expected = Notification.builder().title("Test").build();
        expected.setBody("With body");

        Notification actual = notificationFactory.createNotification("Test", "With body", NotificationAction.builder().build());

        Assert.assertEquals(expected.getBody(), actual.getBody());
    }

    @Test
    public void Factory_WithoutBody_true() {
        Notification expected = Notification.builder().title("No body").build();

        Notification actual = notificationFactory.createNotification("No body", NotificationAction.builder().build());

        Assert.assertEquals(expected.getTitle(), actual.getTitle());
    }
}
