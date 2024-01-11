package com.serenitydojo.wordle.microservices.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersistantPlayerService implements PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
            PasswordHashService passwordHashService;

    /**
     * Register a new player
     */
    public Long registerPlayer(Player newPlayer) {
        if (playerRepository.findByEmail(newPlayer.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("A player with this email already exists");
        }

        newPlayer.setPassword(passwordHashService.hash(newPlayer.getPassword()));

        Player savedPlayer = playerRepository.save(newPlayer);
        return savedPlayer.getId();
    }
}
