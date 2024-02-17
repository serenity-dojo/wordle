package com.serenitydojo.wordle.microservices.registration.service;

import com.serenitydojo.wordle.microservices.domain.Player;
import com.serenitydojo.wordle.microservices.registration.persistance.PlayerRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log
public class PersistantPlayerService implements PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * Register a new player
     */
    public Player registerPlayer(Player playerDetails) {
        if (playerRepository.findByUsername(playerDetails.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("A player with this username already exists");
        }
        if (playerRepository.findByEmail(playerDetails.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("A player with this email already exists");
        }

        log.info("Registering new player: " + playerDetails);
        Player newPlayer = new Player(playerDetails.getUsername(),
                passwordEncoder.encode(playerDetails.getPassword()),
                playerDetails.getEmail(),
                playerDetails.getCountry(),
                playerDetails.isReceiveUpdates());

        return playerRepository.save(newPlayer);
    }

    @Override
    public Optional<Player> findPlayerByUsername(String username) {
        return playerRepository.findByUsername(username);
    }

    @Override
    public List<Player> findAll() {
        return playerRepository.findAll();
    }
}
