package com.vibes.ux.notifications.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vibes.ux.server.CryptoService;
import com.vibes.ux.server.repository.ServerRepository;
import com.vibes.ux.subscribe.domain.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Service("ServerRepository")
@Slf4j
public class NotificationService {
    private final ServerRepository serverRepository;
    private final HttpClient httpClient;
    private final CryptoService cryptoService;

    private final ObjectMapper objectMapper;

    private final Algorithm jwtAlgorithm;

    private static long HOUR = 60 * 60 * 1000;

    private final String subject = "mailto:rick.verkuijlen@vanderlande.com";

    @Autowired
    public NotificationService(ServerRepository serverRepository, CryptoService cryptoService, ObjectMapper objectMapper) {
        this.serverRepository = serverRepository;
        this.cryptoService = cryptoService;

        this.objectMapper = objectMapper;

        this.jwtAlgorithm = Algorithm.ECDSA256(this.serverRepository.getPublicKey(),
                this.serverRepository.getPrivateKey());
        this.httpClient = HttpClient.newHttpClient();
    }

    public void sendPushMessageToAllSubscribers(Map<String, Subscription> subs,
                                                 Object message) throws JsonProcessingException {

        Set<String> failedSubscriptions = new HashSet<>();

        for (Subscription subscription : subs.values()) {
            try {
                byte[] result = this.cryptoService.encrypt(
                        this.objectMapper.writeValueAsString(message),
                        subscription.getSubscriptionKeys().getP256dh(), subscription.getSubscriptionKeys().getAuth(), 0);
                if (!sendPushMessage(subscription, result)) {
                    failedSubscriptions.add(subscription.getEndpoint());
                }
            }
            catch (InvalidKeyException | NoSuchAlgorithmException
                    | InvalidAlgorithmParameterException | IllegalStateException
                    | InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException
                    | BadPaddingException e) {
                log.error("send encrypted message", e);
            }
        }

        failedSubscriptions.forEach(subs::remove);
    }

    private boolean sendPushMessage(Subscription subscription, byte[] body) {
        String origin = null;
        try {
            URL url = new URL(subscription.getEndpoint());
            origin = url.getProtocol() + "://" + url.getHost();
        }
        catch (MalformedURLException e) {
            log.error("create origin", e);
            return false;
        }

        Date today = new Date();
        Date expires = new Date(today.getTime() + 12 * HOUR);

        String token = JWT.create().withAudience(origin).withExpiresAt(expires)
                .withSubject(this.subject).sign(this.jwtAlgorithm);

        URI endpointURI = URI.create(subscription.getEndpoint());

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
        if (body != null) {
            httpRequestBuilder.POST(HttpRequest.BodyPublishers.ofByteArray(body))
                    .header("Content-Type", "application/octet-stream")
                    .header("Content-Encoding", "aes128gcm");
        }
        else {
            httpRequestBuilder.POST(HttpRequest.BodyPublishers.noBody());
        }

        HttpRequest request = httpRequestBuilder.uri(endpointURI).header("TTL", "180")
                .header("Authorization",
                        "vapid t=" + token + ", k=" + this.serverRepository.getPublicKeyBase64())
                .build();

        return handleResponse(subscription, request);
    }

    private boolean handleResponse(Subscription subscription, HttpRequest request) {
        try {
            HttpResponse<Void> response = this.httpClient.send(request,
                    HttpResponse.BodyHandlers.discarding());

            switch (response.statusCode()) {
                case 201:
                    log.info("Push message successfully sent: {}",
                            subscription.getEndpoint());
                    break;
                case 404:
                case 410:
                    log.warn("Subscription not found or gone: {}",
                            subscription.getEndpoint());
                    // remove subscription from our collection of subscriptions
                    return false;
                case 429:
                    log.error("Too many requests: {}", request);
                    break;
                case 400:
                    log.error("Invalid request: {}", request);
                    break;
                case 413:
                    log.error("Payload size too large: {}", request);
                    break;
                default:
                    log.error("Unhandled status code: {} / {}", response.statusCode(),
                            request);
            }
        }
        catch (IOException | InterruptedException e) {
            log.error("send push message", e);
        }

        return true;
    }
}
