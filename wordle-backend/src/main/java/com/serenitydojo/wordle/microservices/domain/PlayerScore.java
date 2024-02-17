package com.serenitydojo.wordle.microservices.domain;

public record PlayerScore(String name, String countryName, long totalTries, long successRate) {}
