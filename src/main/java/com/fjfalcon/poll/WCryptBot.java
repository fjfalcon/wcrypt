package com.fjfalcon.poll;

import com.fjfalcon.api.CurrencyCheck;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

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

            SendMessage message = null;
            if (update.getMessage().getText().startsWith("/wcrypt")) {
                message = new SendMessage() // Create a SendMessage object with mandatory fields
                        .setChatId(update.getMessage().getChatId())
                        .setText(currencyCheck.getMessage());
            }

            if (update.getMessage().getText().startsWith("/petya")) {
                message = new SendMessage() // Create a SendMessage object with mandatory fields
                        .setChatId(update.getMessage().getChatId())
                        .setText(currencyCheck.getPetyaMessage());
            }

            if (update.getMessage().getText().startsWith("/eth")) {
                String incoming[] = update.getMessage().getText().split(" ");

                if (incoming.length > 1) {
                    String wallet = update.getMessage().getText().split(" ")[1];
                    message = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(currencyCheck.getEthBalance(wallet));
                }
            }

            if (update.getMessage().getText().startsWith("/btc")) {
                String incoming[] = update.getMessage().getText().split(" ");

                if (incoming.length > 1) {
                    String wallet = update.getMessage().getText().split(" ")[1];
                    message = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(currencyCheck.getBtcBalance(wallet));
                }
            }

            if (update.getMessage().getText().startsWith("/price btc")) {
                String incoming[] = update.getMessage().getText().split(" ");

                if (incoming.length > 1) {
                    message = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(currencyCheck.getBtcPrice());
                }
            }

            if (update.getMessage().getText().startsWith("/price eth")) {
                String incoming[] = update.getMessage().getText().split(" ");

                if (incoming.length > 1) {
                    message = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(currencyCheck.getEthPrice());
                }
            }



            sendText(message);
        }
    }


    private void sendText(SendMessage sendMessage) {
        if (sendMessage != null)
            try {
                sendMessage(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
    }

    public String getBotUsername() {
        return name;
    }


}