package com.serenitydojo.wordle.model;

import com.serenitydojo.wordle.dictionary.WordleDictionary;

public class LenientDictionary extends WordleDictionary {

    @Override
    public boolean contains(String word) {
        return true;
    }
}
