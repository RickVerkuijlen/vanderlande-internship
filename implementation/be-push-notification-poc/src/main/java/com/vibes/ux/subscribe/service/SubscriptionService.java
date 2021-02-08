package com.vibes.ux.subscribe.service;

import com.vibes.ux.subscribe.domain.Subscription;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SubscriptionService {
    private Map<String, Subscription> subscribers = new ConcurrentHashMap<>();

    public void addSubscriber(Subscription subscription) {
        this.subscribers.put(subscription.getSubscriptionKeys().getAuth(), subscription);
    }

    public void removeSubscriber(String auth) {
        this.subscribers.remove(auth);
    }

    public Subscription getSubscriptionByAuth(String auth) {
        return this.subscribers.get(auth);
    }

    public Map<String, Subscription> getAllSubscribers() {
        return subscribers;
    }
}
