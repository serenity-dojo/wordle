package com.serenitydojo.wordle.microservices.players;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersistantPlayerService implements PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * Register a new player
     */
    public String registerPlayer(PlayerDTO playerDetails) {
        if (playerRepository.findByUsername(playerDetails.username()).isPresent()) {
            throw new EmailAlreadyExistsException("A player with this email already exists");
        }

        Player newPlayer = new Player(playerDetails.username(),
                passwordEncoder.encode(playerDetails.password()),
                playerDetails.email());

        Player savedPlayer = playerRepository.save(newPlayer);
        return savedPlayer.getId();
    }
}
