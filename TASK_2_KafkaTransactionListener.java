package com.jpmc.midascore;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaTransactionListener {
    private static final Logger log = LoggerFactory.getLogger(KafkaTransactionListener.class);
    private final ObjectMapper mapper;

    public KafkaTransactionListener(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core")
    public void onMessage(@Payload String json) throws Exception {
        JsonNode node = mapper.readTree(json);
        double amount = node.hasNonNull("amount") ? node.get("amount").asDouble() : Double.NaN;
        log.info("Received tx amount={}", amount);
        // set breakpoint here to record the first 4 amounts
    }

}
