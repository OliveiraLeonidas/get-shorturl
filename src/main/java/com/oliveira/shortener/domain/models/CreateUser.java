package com.oliveira.shortener.domain.models;

public record CreateUser(
        String email,
        String password,
        String name,
        Role role) {
}