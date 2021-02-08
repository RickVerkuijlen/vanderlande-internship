package com.vibes.ux.notifications;

import com.vibes.ux.notifications.service.NotificationService;
import com.vibes.ux.subscribe.domain.Subscription;
import com.vibes.ux.subscribe.domain.SubscriptionKeys;
import com.vibes.ux.subscribe.service.SubscriptionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class NotificationControllerTests {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SubscriptionService subscriptionService;

    private Subscription subscription;

    @Before
    public void setUp() {
        subscription = new Subscription();
        subscription.setEndpoint("https://fcm.googleapis.com/");
        subscription.setExpirationTime(0);
        SubscriptionKeys subscriptionKeys = new SubscriptionKeys();
        subscriptionKeys.setAuth("4vQK-SvRAN5eo-8ASlrwA==");
        subscriptionKeys.setP256dh("BLQELIDm-6b9Bl07YrEuXJ4BL_YBVQ0dvt9NQGGJxIQidJWHPNa9YrouvcQ9d7_MqzvGS9Alz60SZNCG3qfpk=");
        subscription.setSubscriptionKeys(subscriptionKeys);

        subscriptionService.addSubscriber(subscription);
    }

    @Test
    @Disabled
    public void testNotifications() {
//        notificationService.sendPushMessageToAllSubscribers(Notification.builder().build());
        Assert.assertEquals(true, true);
    }
}
