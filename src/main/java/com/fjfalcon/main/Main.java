package com.fjfalcon.main;

import com.fjfalcon.poll.WCryptBot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by fjfalcon on 14.05.17.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Properties prop = new Properties();
        prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(new WCryptBot(prop.getProperty("token"), prop.getProperty("name"), prop.getProperty("currency_token")));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
