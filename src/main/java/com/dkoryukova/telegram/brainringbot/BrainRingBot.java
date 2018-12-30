package com.dkoryukova.telegram.brainringbot;

import com.dkoryukova.telegram.brainringbot.container.BotContainer;
import com.dkoryukova.telegram.brainringbot.controller.AbstractController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class BrainRingBot extends TelegramLongPollingBot {
    private static BotContainer container = BotContainer.getInstanse();

    @Value("${bot.name}")
    private String name;

    @Value("${bot.token}")
    private String token;

    @Override
    public void onUpdateReceived(Update update) {
        BotApiMethod response = null;
        Long chatId = update.hasMessage() ? update.getMessage().getChatId() :
                update.hasCallbackQuery() ? update.getCallbackQuery().getMessage().getChatId() : 0;

        String path = update.hasMessage() ? update.getMessage().getText() :
                update.hasCallbackQuery() ? update.getCallbackQuery().getData() : "";

        AbstractController controller = container.getBotApiMethodController(path);

        if (controller == null && !path.startsWith("/") && Board.isPlaying(chatId)) {
            controller = container.getBotApiMethodController("/checkAnswer");
        }

        if (controller != null) {
            response = controller.process(update);
        } else {
            response = new SendMessage()
                    .setChatId(chatId)
                    .setText("О чем ты?");
        }

        try {
            execute(response);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
