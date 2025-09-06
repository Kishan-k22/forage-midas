package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {
    private final UserRepository users;
    private final TransactionRecordRepository transactions;

    public DatabaseConduit(UserRepository users, TransactionRecordRepository transactions) {
        this.users = users;
        this.transactions = transactions;
    }

    public void save(UserRecord user) {
        users.save(user);
    }

    public void save(Transaction tx) {
        UserRecord fromUser = queryUser(tx.getSenderId());
        UserRecord toUser = queryUser(tx.getRecipientId());
        TransactionRecord record = new TransactionRecord(fromUser, toUser, tx.getAmount(), tx.getIncentive());
        transactions.save(record);

        fromUser.setBalance(fromUser.getBalance() - tx.getAmount());
        save(fromUser);

        toUser.setBalance(toUser.getBalance() + tx.getAmount() + tx.getIncentive());
        save(toUser);
    }

    public boolean isValid(Transaction tx) {
        UserRecord fromUser = queryUser(tx.getSenderId());
        UserRecord toUser = queryUser(tx.getRecipientId());
        return fromUser != null && toUser != null && fromUser.getBalance() >= tx.getAmount();
    }

    public UserRecord queryUser(Long userId) {
        return users.findById(userId).orElse(null);
    }

    public float queryUserBalance(Long userId) {
        UserRecord user = queryUser(userId);
        return user == null ? 0 : user.getBalance();
    }
}
