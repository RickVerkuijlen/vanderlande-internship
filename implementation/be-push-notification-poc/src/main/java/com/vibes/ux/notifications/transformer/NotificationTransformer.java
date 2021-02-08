package com.vibes.ux.notifications.transformer;

import com.vibes.ux.notifications.domain.Notification;
import com.vibes.ux.notifications.domain.NotificationAction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationTransformer {

    public Notification createNotification(String title, NotificationAction actions) {
        List<NotificationAction> actionList = new ArrayList<>();
        actionList.add(actions);

        return Notification.builder()
                .title(title)
                .actions(actionList)
                .build();
    }

    public Notification createNotification(String title, String body, NotificationAction actions) {
        List<NotificationAction> actionList = new ArrayList<>();
        actionList.add(actions);

        return Notification.builder()
                .title(title)
                .body(body)
                .actions(actionList)
                .build();
    }

    public Notification createNotification(String title, String body) {
        return Notification.builder()
                .title(title)
                .body(body)
                .build();
    }
}
