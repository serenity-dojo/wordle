package com.serenitydojo.wordle.integrationtests.api;

import com.github.javafaker.Faker;
import com.serenitydojo.wordle.microservices.authentication.PasswordHashService;
import com.serenitydojo.wordle.microservices.authentication.Player;
import com.serenitydojo.wordle.microservices.authentication.PlayerRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Registering a new user")
@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = com.serenitydojo.wordle.microservices.WordleApplication.class)
public class RegisteringANewPlayer {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    String id;

    @Steps
    GameFacade gameFacade;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PasswordHashService passwordHashService;

    Faker fake = Faker.instance();

    @Test
    @DisplayName("Players need to register before they can login and play")
    void registeringAsANewPlayer() {
        String name = fake.name().name();
        String email = fake.bothify("????##@gmail.com");
        String password = fake.bothify("????####");

        Player player = new Player(email, password, name);
        Long id = SerenityRest
                .with()
                .body(player)
                .contentType(ContentType.JSON)
                .post("/api/players/register")
                .getBody().as(Long.class);

        assertThat(id).isNotZero();
    }

    @Test
    @DisplayName("Password must not be empty")
    void registeringAsANewPlayerWithAMissingPassowrd() {
        String name = fake.name().name();
        String email = fake.bothify("????##@gmail.com");

        Player player = new Player(email, "", name);
        SerenityRest
                .with()
                .body(player)
                .contentType(ContentType.JSON)
                .post("/api/players/register")
                .then().statusCode(400);
    }

    @Test
    @DisplayName("Password should be stored as a hashed password")
    void hashTheUserPassword() {
        String name = fake.name().name();
        String email = fake.bothify("????##@gmail.com");
        String password = fake.bothify("????####");

        Player player = new Player(email, password, name);
        SerenityRest
                .with()
                .body(player)
                .contentType(ContentType.JSON)
                .post("/api/players/register").then()
                .statusCode(201);

        Optional<Player> savedPlayer = playerRepository.findByEmail(email);

        assertThat(savedPlayer).isPresent();
        assertThat(passwordHashService.check(password, savedPlayer.get().getPassword())).isTrue();
    }

    @Test
    @DisplayName("Email must be unique")
    void registeringAsANewPlayerWithAnExistingEmail() {
        String name = fake.name().name();
        String email = fake.bothify("????##@gmail.com");
        String password = "secret";

        SerenityRest
                .with()
                .body(new Player(email, password, name + " 1"))
                .contentType(ContentType.JSON)
                .post("/api/players/register");

        SerenityRest
                .with()
                .body(new Player(email, password, name + " 2"))
                .contentType(ContentType.JSON)
                .post("/api/players/register")
                .then().statusCode(409);
    }
}
