package com.serenitydojo.wordle.model;

public enum CellColor {
    GREEN("+"), YELLOW("*"), GRAY("-");

    private String symbol;

    CellColor(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
