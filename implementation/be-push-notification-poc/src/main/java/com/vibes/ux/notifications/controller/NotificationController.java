package com.vibes.ux.notifications.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vibes.ux.notifications.domain.Notification;
import com.vibes.ux.notifications.domain.NotificationAction;
import com.vibes.ux.notifications.service.NotificationService;
import com.vibes.ux.subscribe.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/notification")
@Slf4j
@CrossOrigin(origins = {"http://localhost:8081", "https://fe-push-notifications-a8041.web.app"})
public class NotificationController {

    private NotificationService notificationService;
    private final SubscriptionService subscriptionService;

    @Autowired
    public NotificationController(NotificationService notificationService, SubscriptionService subscriptionService) {
        this.notificationService = notificationService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("")
    public boolean Notification() {
        log.info("Notification being send");

        List<NotificationAction> actions = new ArrayList<>();
        actions.add(NotificationAction.builder().title("Acknowledge alert").build());
        actions.add(NotificationAction.builder().title("Close alert").build());
        try {
            Notification not = Notification.builder()
                    .title("VIBES UX")
                    .silent(false)
                    .actions(actions)
                    .vibrate(Arrays.asList(300, 100 ,400))
                    .body("Baggage AB123 is missing!").build();

            notificationService.sendPushMessageToAllSubscribers(subscriptionService.getAllSubscribers(), not);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return true;
    }

}
