package com.serenitydojo.wordle.integrationtests.api;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Wordle Status Check")
@Tag("integration")
@SpringBootTest(classes = com.serenitydojo.wordle.microservices.WordleApplication.class)
@AutoConfigureMockMvc
class ApplicationStatusCheck {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @DisplayName("We can check the status of the Wordle service by sending a GET to /api/service")
    public void checkStatus() {
        RestAssuredMockMvc.get("/api/status")
                .then()
                .statusCode(200);
    }
}
