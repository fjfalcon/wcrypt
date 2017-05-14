package com.fjfalcon.poll;

import com.fjfalcon.btc.BalanceCheck;
import com.fjfalcon.btc.CurrencyCheck;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import jdk.nashorn.internal.parser.Token;

public class WCryptBot extends TelegramLongPollingBot {

    private final String token;
    private final String name;

    private final CurrencyCheck currencyCheck;

    public WCryptBot(String token, String name, String currencyToken) {
        this.token = token;
        this.name = name;
        currencyCheck = new CurrencyCheck(currencyToken);
    }

    public String getBotToken() {
        return token;


    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().equals("/wcrypt")) {
                SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                        .setChatId(update.getMessage().getChatId())
                        .setText(currencyCheck.getMessage());
                try {
                    sendMessage(message); // Call method to send the message
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getBotUsername() {
        return name;
    }


}