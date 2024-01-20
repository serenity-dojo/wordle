package com.serenitydojo.wordle.microservices.registration.service;

import com.serenitydojo.wordle.microservices.domain.Player;
import com.serenitydojo.wordle.microservices.game.PlayerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private PlayerService playerService;

    @Autowired
    public AuthenticationService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public boolean userIsAuthenticated() {
        return AuthenticationContext.getCurrentUser() != null && AuthenticationContext.getCurrentUser().getUsername() != null;
    }

    public Player getCurrentPlayer() {
        if (AuthenticationContext.getCurrentUser() == null || !currentUserIsKnown()) {
            throw new PlayerNotFoundException("No player found for this session");
        }
        return playerService.findPlayerByUsername(AuthenticationContext.getCurrentUser().getUsername()).get();
    }

    private boolean currentUserIsKnown() {
        return playerService.findPlayerByUsername(AuthenticationContext.getCurrentUser().getUsername()).isPresent();
    }
}
