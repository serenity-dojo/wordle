package wordle.api;

import io.restassured.RestAssured;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class WhenCheckingTheServiceStatus {

    @Before
    public void setBaseUrl() {
        RestAssured.baseURI = "http://localhost:9000";
    }

    @Test
    public void checkStatus() {
        SerenityRest.get("/api/status")
                .then()
                .statusCode(200);
    }
}
