package com.trading.tradehub.model;

public record TelegramUserModel(
        Long id,
        Boolean isBot,
        String firstName,
        String lastName,
        String username,
        String languageCode,
        Boolean isPremium,
        Boolean addedToAttachmentMenu,
        Boolean canJoinGroups,
        Boolean canReadAllGroupMessages,
        Boolean supportsInlineQueries
)
{
}
