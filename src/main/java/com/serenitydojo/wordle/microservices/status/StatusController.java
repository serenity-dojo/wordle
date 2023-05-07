package com.serenitydojo.wordle.microservices.status;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class StatusController {

    @RequestMapping(value = "/api/status", method = GET)
    @Operation(description = "Check the status of the API")
    public String status() {
        return "Wordle is running";
    }
}
