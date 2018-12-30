package com.dkoryukova.telegram.brainringbot.controller;

import com.dkoryukova.telegram.brainringbot.annotation.BotController;
import com.dkoryukova.telegram.brainringbot.annotation.BotRequestMapping;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@BotController
public class AdminController {

    @Value("${his.name}")
    private String hisName;

    @Value("${bros}")
    private List<String> bros;

    private static final String GIFT = "\uD83C\uDF81";


    @BotRequestMapping(value = "/start")
    public SendMessage start(Update update) {
        String userName = update.getMessage().getFrom().getUserName();
        Long chatId = update.getMessage().getChatId();
        SendMessage response = new SendMessage()
                .setChatId(chatId);
        if (hisName.equals(userName)||true) {
            response.setText("Привет, Валечек!\n\nЯ твой новогодний подарок" + GIFT + "\n\n" +
                    "Брейн-ринг бот, написанный, чтобы раз и навсегда определить, кто умнее.");
        } else {
            response.setText("Мы начинаем");
        }
        if (bros.contains(userName)) {
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(new InlineKeyboardButton().setText("Начать").setCallbackData("/next"));

            rowsInline.add(rowInline);
            markupInline.setKeyboard(rowsInline);
            response.setReplyMarkup(markupInline);
        } else {
            response.setText("I don't know you!");
        }

        return response;
    }
}
