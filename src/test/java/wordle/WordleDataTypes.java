package wordle;

import io.cucumber.java.DataTableType;
import wordle.model.CellColor;

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
}
