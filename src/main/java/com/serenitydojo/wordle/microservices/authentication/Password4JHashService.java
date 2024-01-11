package com.serenitydojo.wordle.microservices.authentication;

import com.password4j.Hash;
import com.password4j.Password;
import org.springframework.stereotype.Service;

@Service
public class Password4JHashService implements PasswordHashService {

    @Override
    public String hash(String password) {
        return Password.hash(password)
                .addRandomSalt()
                .addPepper("wordle-secret")
                .withArgon2().getResult();
    }

    @Override
    public boolean check(String password, String hash) {
        return Password.check(password, hash)
                .addPepper("wordle-secret")
                .withArgon2();
    }
}
