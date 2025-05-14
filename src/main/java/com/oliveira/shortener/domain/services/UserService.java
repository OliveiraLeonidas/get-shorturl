package com.oliveira.shortener.domain.services;

import com.oliveira.shortener.domain.entities.User;
import com.oliveira.shortener.domain.models.CreateShortUrl;
import com.oliveira.shortener.domain.models.CreateUser;
import com.oliveira.shortener.domain.models.Role;
import com.oliveira.shortener.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(CreateUser createUser) {
        if (userRepository.existsByEmail(createUser.email())) {
            throw new RuntimeException("Email already exists");
        }
        var user = new User();
        user.setEmail(createUser.email());
        user.setPassword(passwordEncoder.encode(createUser.password()));
        user.setName(createUser.name());
        user.setRole(createUser.role());
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
    }
}