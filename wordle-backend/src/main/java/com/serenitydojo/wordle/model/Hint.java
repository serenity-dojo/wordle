package com.serenitydojo.wordle.model;

@FunctionalInterface
public interface Hint {
    String getHint(String word);
}
