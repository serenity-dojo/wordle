package com.serenitydojo.wordle.microservices.game;

import com.serenitydojo.wordle.microservices.domain.GameHistory;
import com.serenitydojo.wordle.microservices.domain.Player;
import com.serenitydojo.wordle.microservices.registration.persistance.GameHistoryRepository;
import com.serenitydojo.wordle.microservices.domain.GameHistoryStatistics;
import com.serenitydojo.wordle.model.GameResult;
import com.serenitydojo.wordle.model.WordleGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameHistoryService {

    @Autowired
    public GameHistoryService(GameHistoryRepository gameHistoryRepository) {
        this.gameHistoryRepository = gameHistoryRepository;
    }

    GameHistoryRepository gameHistoryRepository;

    public void recordGameHistory(Player player, WordleGame wordleGame) {
        GameHistory gameHistory = new GameHistory();
        gameHistory.setPlayer(player);
        gameHistory.setDateTimePlayed(LocalDateTime.now());
        gameHistory.setWon(wordleGame.getResult() == GameResult.WIN);
        gameHistory.setNumberOfGuesses(wordleGame.getRenderedCells().size());
        gameHistoryRepository.save(gameHistory);
    }

    public List<GameHistory> findGameHistory(Player player) {
        return gameHistoryRepository.findByPlayer(player);
    }

    public GameHistoryStatistics calculateStatistics(Player player) {
        List<GameHistory> playerHistory = gameHistoryRepository.findByPlayer(player);

        long totalTries = playerHistory.size();
        long totalWins = playerHistory.stream().filter(GameHistory::getWon).count();

        // Calculate the success rate as a double to maintain precision during calculation
        long percentageSuccessRate = calculatePercentageSuccessRate(totalTries, (double) totalWins);
        long currentWinningStreak = calculateCurrentWinningStreak(playerHistory);
        long bestWinningStreak = calculateBestWinningStreak(playerHistory);
        Map<Integer, Integer> guessDistribution = calculateGuessDistribution(playerHistory);
        return new GameHistoryStatistics(totalTries,
                totalWins,
                percentageSuccessRate,
                currentWinningStreak,
                bestWinningStreak,
                guessDistribution);
    }

    private Map<Integer, Integer> calculateGuessDistribution(List<GameHistory> playerHistory) {
        Map<Integer, Integer> guessDistribution = new HashMap<>();

        // Initialize the map with keys 1 to 6, and their counts set to 0
        for (int i = 1; i <= 6; i++) {
            guessDistribution.put(i, 0);
        }

        playerHistory.stream()
                .filter(GameHistory::getWon)
                .forEach(
                        game -> guessDistribution.put(game.getNumberOfGuesses(),
                                guessDistribution.get(game.getNumberOfGuesses()) + 1)
                );

        return guessDistribution;
    }

    private static long calculateCurrentWinningStreak(List<GameHistory> playerHistory) {
        long winningStreak = 0;

        for (int i = playerHistory.size() - 1; i >= 0; i--) {
            if (playerHistory.get(i).getWon()) {
                winningStreak++;
            } else {
                break;
            }
        }

        return winningStreak;
    }

    private static long calculateBestWinningStreak(List<GameHistory> playerHistory) {
        long currentStreak = 0;
        long bestStreak = 0;

        for (GameHistory game : playerHistory) {
            if (game.getWon()) {
                // Increment current streak if the game is won
                currentStreak++;
                // Update best streak if the current streak is longer
                if (currentStreak > bestStreak) {
                    bestStreak = currentStreak;
                }
            } else {
                // Reset current streak if the game is lost
                currentStreak = 0;
            }
        }

        return bestStreak;
    }

    private static long calculatePercentageSuccessRate(long totalTries, double totalWins) {
        double successRate = 0;
        if (totalTries != 0) {
            successRate = totalWins / totalTries * 100;
        }

        // Round the success rate to the nearest whole number
        long percentageSuccessRate = Math.round(successRate);
        return percentageSuccessRate;
    }
}
