package com.serenitydojo.wordle.domain;

public class GoGame extends Game {
    public GoGame() {
        super("Go",0);
    }

    @Override
    public String describe() {
//        return getName() + " starts with " + getInitialNumberOfPieces() + " pieces and has no limit on the number of pieces";
        return super.describe() + " and has no limit on the number of pieces";
    }
}
