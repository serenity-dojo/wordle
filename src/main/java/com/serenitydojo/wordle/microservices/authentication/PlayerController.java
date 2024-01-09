package com.serenitydojo.wordle.microservices.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Long registerUser(@RequestBody Player player) {
        return playerService.registerPlayer(player);
    }
}
