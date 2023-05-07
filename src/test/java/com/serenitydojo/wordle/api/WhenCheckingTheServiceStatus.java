package com.serenitydojo.wordle.api;

import io.restassured.RestAssured;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SerenityJUnit5Extension.class)
@Tag("integration")
public class WhenCheckingTheServiceStatus {

    @BeforeEach
    public void setBaseUrl() {
        RestAssured.baseURI = "http://localhost:9000";
    }

    @Test
    @DisplayName("We can check the status of the Wordle service hy sending a GET to /api/service")
    public void checkStatus() {
        SerenityRest.get("/api/status")
                .then()
                .statusCode(200);
    }
}
