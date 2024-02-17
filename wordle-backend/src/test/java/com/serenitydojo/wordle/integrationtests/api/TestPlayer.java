package com.serenitydojo.wordle.integrationtests.api;

import com.github.javafaker.Faker;
import com.serenitydojo.wordle.microservices.domain.Player;

public record TestPlayer(String name, String email, String password) {
    public static TestPlayer randomPlayer() {
        Faker fake = Faker.instance();
        String name = fake.name().username();
        String email = fake.bothify("????##@gmail.com");
        String password = fake.bothify("????####");

        return new TestPlayer(name, email, password);
    }

    public Player asPlayer() {
        return new Player(name, password, email,"UK",false);
    }
}
