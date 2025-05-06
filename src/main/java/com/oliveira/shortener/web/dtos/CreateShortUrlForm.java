package com.oliveira.shortener.web.dtos;


import jakarta.validation.constraints.NotBlank;

public record CreateShortUrlForm(
        @NotBlank(message = "Original URL is required") String originalUrl) {
}
