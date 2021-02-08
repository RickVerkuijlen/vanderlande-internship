package com.vibes.ux.subscribe.controller;

import com.google.gson.Gson;
import com.vibes.ux.server.repository.ServerRepository;
import com.vibes.ux.subscribe.domain.Subscription;
import com.vibes.ux.subscribe.service.SubscriptionService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;


@RestController
@RequestMapping("/subscribe")
@Slf4j
@Api(tags = "Subscription controller", description = "Controller to let users control their subscription")
@CrossOrigin(origins = {"http://localhost:8081", "https://fe-push-notifications-a8041.web.app"})
public class SubscriptionController {

    private final ServerRepository serverKeys;

    private final SubscriptionService subscriptionService;

    private final Gson gson;

    public SubscriptionController(ServerRepository serverKeys, SubscriptionService subscriptionService) {
        this.serverKeys = serverKeys;
        this.subscriptionService = subscriptionService;

        gson = new Gson();
    }

    @GetMapping(path = "/publicSigningKey", produces = "application/octet-stream")
    public byte[] publicSigningKey() {
        return this.serverKeys.getPublicKeyUncompressed();
    }

    @GetMapping(path = "/publicSigningKeyBase64")
    public String publicSigningKeyBase64() {
        log.info("Get public signing key Base64");
        return gson.toJson(this.serverKeys.getPublicKeyBase64());
    }

    @GetMapping
    public @ResponseBody
    HttpEntity<List<Subscription>> getAllSubscribers() {
        return new ResponseEntity(this.subscriptionService.getAllSubscribers(), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void subscribe(@RequestBody Subscription subscription) {
        log.info("Subscriber coming in: " + subscription.getEndpoint());
        this.subscriptionService.addSubscriber(subscription);
    }

    @DeleteMapping("/unsubscribe/{auth}")
    @ResponseStatus(HttpStatus.OK)
    public void unsubscribe(@PathVariable("auth") String auth) {
        this.subscriptionService.removeSubscriber(auth);
    }

    @PostMapping("/isSubscribed")
    public boolean isSubscribed(@RequestBody Subscription subscription) {
        return this.subscriptionService.getAllSubscribers().containsKey(subscription.getEndpoint());
    }
}
