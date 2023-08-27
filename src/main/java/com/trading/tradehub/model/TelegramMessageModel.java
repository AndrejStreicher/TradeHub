package com.trading.tradehub.model;

import java.util.List;

public record TelegramMessageModel(
        Integer messageId,
        Integer messageThreadId,
        TelegramUserModel from,
        TelegramChatModel senderChat,
        Integer date,
        TelegramChatModel chat,
        String text,
        List<TelegramMessageEntityModel> entities)
{
}
