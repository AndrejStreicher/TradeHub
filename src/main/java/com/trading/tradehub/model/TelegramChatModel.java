package com.trading.tradehub.model;

public record TelegramChatModel(
        Long id,
        String type,
        String title,
        String username,
        String firstName,
        String lastName,
        Boolean isForum)
{

}
