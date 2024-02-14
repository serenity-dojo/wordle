package com.serenitydojo.wordle.microservices.domain;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public record GameHistoryDTO(LocalDateTime dateTimePlayed, Boolean outcome, Integer numberOfGuesses) {}
