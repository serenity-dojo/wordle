package com.serenitydojo.wordle.microservices.registration.service;

import com.serenitydojo.wordle.microservices.domain.Player;
import com.serenitydojo.wordle.microservices.registration.persistance.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersistantPlayerService implements PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * Register a new player
     */
    public String registerPlayer(Player playerDetails) {
        if (playerRepository.findByUsername(playerDetails.getUsername()).isPresent()) {
            throw new EmailAlreadyExistsException("A player with this email already exists");
        }

        Player newPlayer = new Player(playerDetails.getUsername(),
                passwordEncoder.encode(playerDetails.getPassword()),
                playerDetails.getEmail());

        Player savedPlayer = playerRepository.save(newPlayer);
        return savedPlayer.getId();
    }

    @Override
    public Optional<Player> findPlayerByUsername(String username) {
        return playerRepository.findByUsername(username);
    }
}
