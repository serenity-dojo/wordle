package com.serenitydojo.wordle.microservices.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerDetailsService implements UserDetailsService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public PlayerDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Player> player = playerRepository.findByEmail(username);
        if (player.isPresent()) {
            return new PlayerDetails(player.get());
        } else {
            throw new UsernameNotFoundException("Player not found");
        }
    }
}
