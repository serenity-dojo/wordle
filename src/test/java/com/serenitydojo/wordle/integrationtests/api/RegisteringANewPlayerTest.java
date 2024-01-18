package com.serenitydojo.wordle.integrationtests.api;

import com.github.javafaker.Faker;
import com.serenitydojo.wordle.microservices.domain.Player;
import com.serenitydojo.wordle.microservices.registration.dto.PlayerDTO;
import com.serenitydojo.wordle.microservices.persistance.PlayerRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Registering a new user")
@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = com.serenitydojo.wordle.microservices.WordleApplication.class)
public class RegisteringANewPlayerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    Faker fake = Faker.instance();

    @Test
    @DisplayName("Players need to register before they can login and play")
    void registeringAsANewPlayer() {
        String name = fake.name().username();
        String email = fake.bothify("????##@gmail.com");
        String password = fake.bothify("????####");

        Player player = new Player(name, password, email);
        SerenityRest
                .with()
                .body(player)
                .contentType(ContentType.JSON)
                .post("/wordle/api/auth/register")
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("Players can log on with their username and password")
    void loggingOn() {
        String name = fake.name().username();
        String email = fake.bothify("????##@gmail.com");
        String password = fake.bothify("????####");

        Player player = new Player(name, password, email);
        SerenityRest
                .given()
                .body(player)
                .contentType(ContentType.JSON)
                .post("/wordle/api/auth/register")
                .then().statusCode(201);

        String token = SerenityRest
                .given()
                .body(new Credentials(player.getUsername(), player.getPassword()))
                .contentType(ContentType.JSON)
                .post("/wordle/api/auth/login")
                .getBody().asString();

        assertThat(token).isNotEmpty();
    }

    @Test
    @DisplayName("Logged-on players can access the APIs")
    void accessingSecureAPIs() {
        String name = fake.name().username();
        String email = fake.bothify("????##@gmail.com");
        String password = fake.bothify("????####");

        Player player = new Player(name, password, email);
        SerenityRest
                .given()
                .body(player)
                .contentType(ContentType.JSON)
                .post("/wordle/api/auth/register")
                .then().statusCode(201);

        JWTToken token = SerenityRest
                .given()
                .body(new Credentials(player.getUsername(), player.getPassword()))
                .contentType(ContentType.JSON)
                .post("/wordle/api/auth/login")
                .getBody().as(JWTToken.class);

        assertThat(token.accessToken()).isNotEmpty();

        SerenityRest
                .given()
                .header("Authorization","Bearer " + token.accessToken())
                .body(player)
                .contentType(ContentType.JSON)
                .post("/wordle/api/game")
                .then().statusCode(200);

    }

    @Test
    @DisplayName("Non-authentication access to APIs is not allowed")
    void accessingSecureAPIsWithoutAJWTToken() {
        String name = fake.name().username();
        String email = fake.bothify("????##@gmail.com");
        String password = fake.bothify("????####");

        Player player = new Player(name, password, email);
        SerenityRest
                .given()
                .body(player)
                .contentType(ContentType.JSON)
                .post("/wordle/api/auth/register")
                .then().statusCode(201);

        SerenityRest
                .given()
                .body(player)
                .contentType(ContentType.JSON)
                .post("/wordle/api/game")
                .then().statusCode(403);

    }

    @Test
    @DisplayName("Password must not be empty")
    void registeringAsANewPlayerWithAMissingPassowrd() {
        String name = fake.name().username();
        String email = fake.bothify("????##@gmail.com");

        PlayerDTO player = new PlayerDTO(name, "", email);
        SerenityRest
                .with()
                .body(player)
                .contentType(ContentType.JSON)
                .post("/wordle/api/auth/register")
                .then().statusCode(400);
    }

    @Test
    @DisplayName("Password should be stored as a hashed password")
    void hashTheUserPassword() {
        String name = fake.name().username();
        String email = fake.bothify("????##@gmail.com");
        String password = fake.bothify("????####");

        Player player = new Player(name, password, email);
        SerenityRest
                .with()
                .body(player)
                .contentType(ContentType.JSON)
                .post("/wordle/api/auth/register")
                .then()
                .statusCode(201);

        Optional<Player> savedPlayer = playerRepository.findByUsername(name);

        assertThat(savedPlayer).isPresent();

        assertThat(passwordEncoder.matches(password, savedPlayer.get().getPassword())).isTrue();
    }

    @Test
    @DisplayName("Email must be unique")
    void registeringAsANewPlayerWithAnExistingEmail() {
        String name = fake.name().username();
        String email = fake.bothify("????##@gmail.com");
        String password = "secret";

        SerenityRest
                .with()
                .body(new Player(email, password, name + " 1"))
                .contentType(ContentType.JSON)
                .post("/wordle/api/auth/register");

        SerenityRest
                .with()
                .body(new Player(email, password, name + " 2"))
                .contentType(ContentType.JSON)
                .post("/wordle/api/auth/register")
                .then().statusCode(409);
    }
}
