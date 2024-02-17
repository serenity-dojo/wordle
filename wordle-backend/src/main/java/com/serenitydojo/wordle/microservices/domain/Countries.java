package com.serenitydojo.wordle.microservices.domain;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Countries {

        private static final Map<String, String> countryNamesByCode= new HashMap<>();

        static {
            for (String iso : Locale.getISOCountries()) {
                Locale countryLocale = new Locale("", iso);
                countryNamesByCode.put(iso, countryLocale.getDisplayCountry());
            }
        }

        public static String findCountryName(String countryCode) {
            return countryNamesByCode.get(countryCode);
        }
}
