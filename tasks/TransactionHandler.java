package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransactionHandler {

    private static final Logger log = LoggerFactory.getLogger(TransactionHandler.class);
    private final DatabaseConduit conduit;
    private final IncentiveQuerier incentiveService;

    public TransactionHandler(DatabaseConduit conduit, IncentiveQuerier incentiveService) {
        this.conduit = conduit;
        this.incentiveService = incentiveService;
    }

    public void process(Transaction tx) {
        if (conduit.isValid(tx)) {
            Incentive inc = incentiveService.fetch(tx);
            tx.setIncentive(inc.getAmount());
            conduit.save(tx);
            log.info("Transaction processed for user {} -> {}", tx.getSenderId(), tx.getRecipientId());
        } else {
            log.warn("Invalid transaction attempt by user {}", tx.getSenderId());
        }
    }
}
