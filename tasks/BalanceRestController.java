package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Balance;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class BalanceRestController {

    private DatabaseConduit conduit;

    public BalanceRestController(DatabaseConduit conduit) {
        this.conduit = conduit;
    }

    @GetMapping("/balance")
    public Balance getUserBalance(@RequestParam("userId") Long userId) {
        float amount = conduit.queryUserBalance(userId);
        return new Balance(amount);
    }
}
