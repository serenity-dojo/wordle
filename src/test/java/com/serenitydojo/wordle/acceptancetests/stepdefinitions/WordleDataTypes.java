package com.serenitydojo.wordle.acceptancetests.stepdefinitions;

import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import com.serenitydojo.wordle.model.CellColor;
import com.serenitydojo.wordle.model.GameResult;

import java.util.List;
import java.util.stream.Collectors;

public class WordleDataTypes {
    @DataTableType
    public String attempt(List<String> cellsInRow) {
        return String.join("", cellsInRow);
    }

    @DataTableType
    public List<CellColor> renderedCells(List<String> cellsInRow) {
        return cellsInRow.stream()
                .map(CellColor::withSymbol)
                .collect(Collectors.toList());
    }

    @ParameterType(".*")
    public GameResult gameResult(String value) {
        switch (value) {
            case "win" : return GameResult.WIN;
            case "lose" : return GameResult.LOSE;
            case "in progress" : return GameResult.IN_PROGRESS;
        }
        throw new IllegalArgumentException("Unknown game result: " + value);
    }
}
