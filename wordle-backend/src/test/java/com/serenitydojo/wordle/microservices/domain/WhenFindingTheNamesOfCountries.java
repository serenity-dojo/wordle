package com.serenitydojo.wordle.microservices.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class WhenFindingTheNamesOfCountries {
    @ParameterizedTest
    @CsvSource({
            "US,United States",
            "CA,Canada",
            "PL,Poland",
            "AU,Australia"
    }
    )
    void shouldFindTheNameOfACountryByCode(String code, String name) {
        assertThat(Countries.findCountryName(code)).isEqualTo(name);
    }
}
