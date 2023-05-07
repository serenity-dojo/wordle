package com.serenitydojo.wordle.domain;

public class Game {

    private final String name;
    private final int initialNumberOfPieces;

    public Game(String name, int initialNumberOfPieces) {
        this.name = name;
        this.initialNumberOfPieces = initialNumberOfPieces;
    }

    public String getName() {
        return name;
    }

    public int getInitialNumberOfPieces() {
        return this.initialNumberOfPieces;
    }

    public String describe() {
        return getName() + " starts with " + getInitialNumberOfPieces() + " pieces";
    }
}
