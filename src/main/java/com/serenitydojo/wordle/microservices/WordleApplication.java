package com.serenitydojo.wordle.microservices;

import com.github.lalyos.jfiglet.FigletFont;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WordleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordleApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println(FigletFont.convertOneLine("WORDLE APP"));
            System.out.println("WORDLE APP STARTED");
        };
    }
}
