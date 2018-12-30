package com.dkoryukova.telegram.brainringbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@SpringBootApplication
public class BrainRingBotApplication implements CommandLineRunner {

    @Autowired
    private BrainRingBot brainRingBot;

    static {
        ApiContextInitializer.init();
    }

    public static void main(String[] args) {
        SpringApplication.run(BrainRingBotApplication.class, args);
    }

    @Override
    public void run(String... args) {
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(brainRingBot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

    }

}

