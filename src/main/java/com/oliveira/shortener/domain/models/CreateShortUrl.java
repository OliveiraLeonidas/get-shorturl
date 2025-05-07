package com.oliveira.shortener.domain.models;

public record CreateShortUrl(
        String originalUrl,
        Boolean isPrivate,
        Integer expirationInDays,
        Long userId
) {
}
