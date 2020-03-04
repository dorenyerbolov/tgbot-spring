package com.deathparade.telegrambot;

import com.deathparade.telegrambot.domain.ExchangeRate;
import com.deathparade.telegrambot.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MoneyExchangeBot extends TelegramLongPollingBot {

    @Autowired
    private ExchangeRateService exchangeRateService;

    private static final String BOT_TOKEN = "1092390065:AAHC0ByXs8nKYjJCIpN4iIOHSP0Q-5DvlHg";
    private static final String BOT_NAME = "moneyexchangebot";

    static {
        ApiContextInitializer.init();
    }

    @PostConstruct
    public void registerBot() {

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        // we check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message msg = update.getMessage();
            String msgContent = msg.getText();
            if (msgContent.equals("EUR") || msgContent.equals("USD") || msgContent.equals("RUB"))
                sendMsg(msg.getChatId(), handleOptional(exchangeRateService.read(msgContent)));
            else if (msgContent.equals("/start"))
                sendMsg(msg.getChatId(), "Welcome! If you want to use this bot you need to type in one of those values: " +
                        "EUR | USD | RUB ");
            else
                sendMsg(msg.getChatId(), "No such command found!");
        }
    }

    private String handleOptional(Optional<ExchangeRate> exchangeRate) {
        return exchangeRate.isPresent() ? exchangeRate.get().toString() : "No such entry in DB";
    }

    private synchronized void sendMsg(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setButtons(SendMessage sendMessage) {
        // here we create a keyboard
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);


        List<KeyboardRow> keyboard = new ArrayList<>();

        // first row in keyboard with corresponding button
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("USD"));
        keyboard.add(keyboardFirstRow);

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("EUR"));
        keyboard.add(keyboardSecondRow);

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(new KeyboardButton("RUB"));
        keyboard.add(keyboardThirdRow);


        // setting keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
