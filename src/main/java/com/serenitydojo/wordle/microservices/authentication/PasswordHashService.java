package com.serenitydojo.wordle.microservices.authentication;

import org.springframework.stereotype.Service;

@Service
public interface PasswordHashService {
    String hash(String password);
    boolean check(String hash, String password);
}
