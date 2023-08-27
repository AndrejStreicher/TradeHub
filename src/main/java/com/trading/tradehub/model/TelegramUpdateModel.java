package com.trading.tradehub.model;

public record TelegramUpdateModel(
        Integer updateId,
        TelegramMessageModel message,
        TelegramMessageModel editedMessage,
        TelegramMessageModel channelPost,
        TelegramMessageModel editedChannelPost
)
{
}
