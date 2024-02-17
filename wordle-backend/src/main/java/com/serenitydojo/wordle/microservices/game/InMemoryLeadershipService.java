package com.serenitydojo.wordle.microservices.game;

import com.serenitydojo.wordle.microservices.domain.Countries;
import com.serenitydojo.wordle.microservices.domain.GameHistoryStatistics;
import com.serenitydojo.wordle.microservices.domain.Player;
import com.serenitydojo.wordle.microservices.domain.PlayerScore;
import com.serenitydojo.wordle.microservices.registration.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InMemoryLeadershipService implements LeaderboardService {

    PlayerService playerService;
    GameHistoryService gameHistoryService;

    @Autowired
    public InMemoryLeadershipService(PlayerService playerService,
                                     GameHistoryService gameHistoryService) {
        this.playerService = playerService;
        this.gameHistoryService = gameHistoryService;
    }

    @Override
    public List<PlayerScore> getLeaderboard() {
        return playerService.findAll()
                .stream()
                .map(this::getPlayerScoreFrom)
                .sorted(Comparator.comparing(PlayerScore::successRate).reversed())
                .collect(Collectors.toList());
    }

    private PlayerScore getPlayerScoreFrom(Player player) {
        GameHistoryStatistics stats = gameHistoryService.calculateStatistics(player);
        return new PlayerScore(player.getUsername(),
                Countries.findCountryName(player.getCountry()),
                stats.totalTries(),
                stats.percentageSuccessRate());
    }
}
