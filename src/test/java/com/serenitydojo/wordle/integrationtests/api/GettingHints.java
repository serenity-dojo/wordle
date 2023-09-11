package com.serenitydojo.wordle.integrationtests.api;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Getting hints")
@Tag("integration")
@SpringBootTest(classes = com.serenitydojo.wordle.microservices.WordleApplication.class)
@AutoConfigureMockMvc
public class GettingHints {

    @Autowired
    private MockMvc mockMvc;

    String id;

    @Steps
    GameFacade gameFacade;

    @BeforeEach
    void newGame() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        id = gameFacade.newGame();
    }

    @Test
    @DisplayName("We can request a hint for the current game via GET /api/game/{id}/hint")
    void requestingAHint() {
        id = gameFacade.newGameWith("BLAND");
        gameFacade.requestHint(id).then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @ParameterizedTest(name = "Hints should include \"{0}\"")
    @ValueSource(strings = {
            "The word starts with the letter B",
            "The word ends with the letter D",
            "The word contains 1 vowel"})
    void requestingAnotherHint(String expectedHint) {
        id = gameFacade.newGameWith("BLAND");
        gameFacade.requestHint(id);
        List<String> hints = gameFacade.requestHint(id).then().extract().as(ArrayList.class);
        assertThat(hints).contains(expectedHint);
    }
}
