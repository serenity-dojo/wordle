package com.serenitydojo.wordle.microservices.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersistantPlayerService implements PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    /**
     * Register a new player
     */
    public Long registerPlayer(Player newPlayer) {
        if (playerRepository.findByEmail(newPlayer.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("A player with this email already exists");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPlayer.getPassword());
        newPlayer.setPassword(encodedPassword);

        Player savedPlayer = playerRepository.save(newPlayer);
        return savedPlayer.getId();
    }
}
