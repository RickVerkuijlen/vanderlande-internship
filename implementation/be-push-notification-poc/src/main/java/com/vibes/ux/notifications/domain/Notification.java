package com.vibes.ux.notifications.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    private String title;

    private Object data;

    private String body;

    private String icon;

    private String image;

    private String language;

    private Boolean renotify;

    private Boolean requireInteraction;

    private Boolean silent;

    private String tag;

    private List<Integer> vibrate;

    private Long timestamp;

    private List<NotificationAction> actions;
}
