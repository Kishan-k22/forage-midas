package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IncentiveQuerier {

    private final RestTemplate client;
    private final String apiUrl;

    public IncentiveQuerier(RestTemplateBuilder restTemplateBuilder,
                            @Value("${general.incentive-api-url}") String apiUrl) {
        this.client = restTemplateBuilder.build();
        this.apiUrl = apiUrl;
    }

    public Incentive fetch(Transaction tx) {
        return client.postForObject(apiUrl, tx, Incentive.class);
    }
}
