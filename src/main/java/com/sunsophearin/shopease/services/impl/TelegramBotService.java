package com.sunsophearin.shopease.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Service
public class TelegramBotService implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final String botToken;
    private final String chatId;

    public TelegramBotService(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.chat-id}") String chatId
    ) {
        this.botToken = botToken;
        this.chatId = chatId;
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    // SpringLongPollingBot interface
    @Override
    public String getBotToken() {
        return botToken;
    }

    // SpringLongPollingBot interface
    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this; // Use this class as the consumer
    }

    // LongPollingSingleThreadUpdateConsumer interface
    @Override
    public void consume(Update update) {
        // Handle incoming updates if you want
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long userChatId = update.getMessage().getChatId();
            log.info("Received message from {}: {}", userChatId, text);

            // Example: Echo the message back
            SendMessage reply = SendMessage.builder()
                    .chatId(userChatId)
                    .text("You said: " + text)
                    .build();
            try {
                telegramClient.execute(reply);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Your custom notification method
    public void sendOrderNotification(String message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .parseMode("HTML") // <-- REQUIRED for beautiful formatting!
                .build();
        try {
            telegramClient.execute(sendMessage);
            log.info("Sent Telegram notification: {}", message);
        } catch (Exception e) {
            log.error("Failed to send Telegram notification: {}", e.getMessage(), e);
        }
    }

}
