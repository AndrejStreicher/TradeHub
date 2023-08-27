package com.trading.tradehub.model;

public record TelegramMessageEntityModel(
        String type,
        Integer offset,
        Integer length,
        String url,
        TelegramUserModel user,
        String language,
        String customEmojiId
)
{
}
