package com.serenitydojo.wordle.microservices.domain;

import java.util.Map;

public record GameHistoryStatistics(
        long totalTries,
        long totalWins,
        long percentageSuccessRate,
        long currentWinningStreak,
        long bestWinningStreak,
        Map<Integer, Integer> guessDistribution
) {}
