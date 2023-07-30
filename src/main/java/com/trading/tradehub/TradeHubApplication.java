package com.trading.tradehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.trading.tradehub.model", "com.trading.tradehub.controller", "com.trading.tradehub.service"})
@EnableScheduling
public class TradeHubApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(TradeHubApplication.class, args);
    }

}
