package com.serenitydojo.wordle.model;

import java.util.Arrays;

public enum CellColor {
    GREEN("*"), YELLOW("+"), GRAY("-"), WHITE(" ");

    private final String symbol;

    CellColor(String symbol) {
        this.symbol = symbol;
    }


    public static CellColor withSymbol(String symbol) {
        return Arrays.stream(CellColor.values())
                .filter(cellColor -> cellColor.symbol.equals(symbol))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown symbol " + symbol));
    }
}
