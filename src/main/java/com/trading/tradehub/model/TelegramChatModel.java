package com.trading.tradehub.model;

public record TelegramChatModel(
        Integer id,
        String type,
        String title,
        String username,
        String firstName,
        String lastName,
        Boolean isForum)
{

}
