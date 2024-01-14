package com.serenitydojo.wordle.microservices.players;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class RegistrationController {
    @Autowired
    private PlayerService playerService;

    @PostMapping("/api/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerUser(@RequestBody PlayerDTO player) {
        if (player.password() == null || player.password().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be empty");
        }
        return playerService.registerPlayer(player);
    }
}
