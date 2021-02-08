package com.vibes.ux.subscribe;

import com.google.gson.Gson;
import com.vibes.ux.subscribe.domain.Subscription;
import com.vibes.ux.subscribe.domain.SubscriptionKeys;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SubscriptionControllerTests {

    Gson gson;
    Subscription subscription;

    @Before
    public void setUp() {
        gson = new Gson();
        subscription = new Subscription();
        subscription.setEndpoint("https://fcm.googleapis.com/");
        subscription.setExpirationTime(0);
        SubscriptionKeys subscriptionKeys = new SubscriptionKeys();
        subscriptionKeys.setAuth("4vQK-SvRAN5eo-8ASlrwA==");
        subscriptionKeys.setP256dh("BLQELIDm-6b9Bl07YrEuXJ4BL_YBVQ0dvt9NQGGJxIQidJWHPNa9YrouvcQ9d7_MqzvGS9Alz60SZNCG3qfpk=");
        subscription.setSubscriptionKeys(subscriptionKeys);
    }

    @Test
    public void setSubscription() {
        String jsonValue = gson.toJson(this.subscription);
        System.out.println(jsonValue);
        given()
                .contentType(ContentType.JSON)
                .body(jsonValue)
                .post("/subscribe")
                .then()
                .statusCode(201)
                .extract()
                .response();
    }

    @Test
    public void deleteSubscription() {
        given()
                .contentType(ContentType.JSON)
                .delete("/subscribe/unsubscribe/4vQK-SvRAN5eo-8ASlrwA==")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}
