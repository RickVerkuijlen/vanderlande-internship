package com.vibes.ux.notifications.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationAction {
    private String action;
    private String title;
    private String icon;
}
