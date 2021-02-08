package com.vibes.ux.subscribe.domain;

import lombok.Data;

@Data
public class Subscription {
    private String endpoint;
    private long expirationTime;
    private SubscriptionKeys subscriptionKeys;
}
