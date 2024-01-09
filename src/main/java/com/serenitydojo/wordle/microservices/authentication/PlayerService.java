package com.serenitydojo.wordle.microservices.authentication;

import com.serenitydojo.wordle.model.CellColor;
import com.serenitydojo.wordle.model.GameResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlayerService {
    /**
     * Register a new player
     */
    Long registerPlayer(Player newPlayer);
}
