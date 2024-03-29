package com.serenitydojo.wordle.microservices.registration.service;

import com.serenitydojo.wordle.microservices.domain.Player;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PlayerService {
    /**
     * Register a new player
     */
    Player registerPlayer(Player newPlayer);

    Optional<Player> findPlayerByUsername(String username);

    List<Player> findAll();
}
