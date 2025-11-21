package com.example.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SmsApiClient {

    private final WebClient client;
    private final String baseUrl;
    private final String apiKey;

    public SmsApiClient(
            WebClient.Builder clientBuilder

    ) {
        this.client = clientBuilder.build();
        this.baseUrl = "http://dosipa.univ-brest.fr";
        this.apiKey = "DOSITPDJF";
    }

    // -----------------------
    //        GET /ping
    // -----------------------
    public String ping() {
        return client.get()
                .uri(baseUrl + "/ping")
                .header("x-api-key", apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    // -----------------------
    //     POST /send-sms
    // -----------------------
    public record SmsResponse(String value) {}

    public SmsResponse sendSms(String to, String message) {
        SmsRequest payload = new SmsRequest(to, message);

        return client.post()
                .uri(baseUrl + "/send-sms")
                .header("x-api-key", apiKey)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(SmsResponse.class)
                .block();
    }

    // DTO
    public record SmsRequest(String to, String message) {}
}
