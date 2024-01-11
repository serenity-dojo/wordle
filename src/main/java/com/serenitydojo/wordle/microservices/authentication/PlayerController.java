package com.serenitydojo.wordle.microservices.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Long registerUser(@RequestBody Player player) {
        if (player.getPassword() == null || player.getPassword().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be empty");
        }
        return playerService.registerPlayer(player);
    }
}
