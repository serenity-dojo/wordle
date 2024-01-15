package com.serenitydojo.wordle.microservices.players;

import org.springframework.stereotype.Service;

@Service
public interface PlayerService {
    /**
     * Register a new player
     */
    String registerPlayer(Player newPlayer);
}
