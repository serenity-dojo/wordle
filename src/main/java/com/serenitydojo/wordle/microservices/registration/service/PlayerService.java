package com.serenitydojo.wordle.microservices.registration.service;

import com.serenitydojo.wordle.microservices.domain.Player;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PlayerService {
    /**
     * Register a new player
     */
    String registerPlayer(Player newPlayer);

    Optional<Player> findPlayerByUsername(String username);
}
