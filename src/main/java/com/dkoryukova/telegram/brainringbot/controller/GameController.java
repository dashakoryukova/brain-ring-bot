package com.dkoryukova.telegram.brainringbot.controller;

import com.dkoryukova.telegram.brainringbot.Board;
import com.dkoryukova.telegram.brainringbot.QuestionPool;
import com.dkoryukova.telegram.brainringbot.annotation.BotController;
import com.dkoryukova.telegram.brainringbot.annotation.BotRequestMapping;
import com.dkoryukova.telegram.brainringbot.model.BotRequestMethod;
import com.dkoryukova.telegram.brainringbot.model.Question;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@BotController
public class GameController {
    private static final int MAX_DISTANCE_DIFF_RATIO = 10;

    @Autowired
    private QuestionPool pool;

    @BotRequestMapping(method = BotRequestMethod.CALLBACK, value = "/next")
    public SendMessage next(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        Question question = pool.getQuestion();

        Board.startRound(chatId, question);

        return new SendMessage()
                .setChatId(chatId)
                .setText("Вопрос:\n\n" + question.getText())
                .setReplyMarkup(getMarkup(Map.of("Сдаюсь", "/answer", "Пропустить", "/next")))
                .enableMarkdown(true);
    }

    @BotRequestMapping(method = BotRequestMethod.CALLBACK, value = "/answer")
    public SendMessage answer(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        if (!Board.isPlaying(chatId)) {
            return new SendMessage()
                    .setChatId(chatId)
                    .setText("Вопрос сыгран");
        }


        Question question = Board.getQuestion(chatId);
        String comment = !"".equals(question.getComment()) ? "Комментарий: " + question.getComment() : "";

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton()
                .setText("Дальше")
                .setCallbackData("/next"));

        if (Board.getQuestion(chatId).isAttempted()) {
            rowInline.add(new InlineKeyboardButton().setText("То же самое\uD83E\uDD2C").setCallbackData("/appelation"));
        }

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);

        Board.completeRound(chatId, false);
        return new SendMessage()
                .setChatId(chatId)
                .setText("Ответ: " + question.getAnswer() + "\n" + comment)
                .setReplyMarkup(markupInline);
    }

    @BotRequestMapping(method = BotRequestMethod.CALLBACK, value = "/appelation")
    public SendMessage appelation(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        Board.appelation(chatId);

        return new SendMessage()
                .setChatId(chatId)
                .setText("Засчитано")
                .setReplyMarkup(getMarkup(Map.of("Дальше", "/next")));
    }

    @BotRequestMapping(method = BotRequestMethod.MESSAGE, value = "/checkAnswer")
    public SendMessage checkAnswer(Update update) {
        long chatId = update.getMessage().getChatId();
        String answer = update.getMessage().getText().trim().toLowerCase();

        String etalon = Board.getQuestion(chatId).getAnswer().toLowerCase().replaceAll("[^ а-я]", "").trim();

        final int threshold = Math.round(MAX_DISTANCE_DIFF_RATIO * etalon.length());
        final LevenshteinDistance levenshteinDistance = new LevenshteinDistance(threshold);

        if (((double) levenshteinDistance.apply(etalon, answer) / etalon.length()) < 0.3) {
            Board.completeRound(chatId, true);
            return new SendMessage()
                    .setChatId(chatId)
                    .setText("Молодец, это верный ответ\uD83D\uDD25\uD83D\uDCAA\n\n" +
                            "Всего ответов:" + Board.getTotalPlayed(chatId) + ", из них правильных:" + Board.getTotalWon(chatId))
                    .setReplyMarkup(getMarkup(Map.of("Дальше", "/next")));
        } else {
            Board.getQuestion(chatId).setAttempted(true);
            return new SendMessage()
                    .setChatId(chatId)
                    .setText("Ноуп");
        }
    }

    private InlineKeyboardMarkup getMarkup(Map<String, String> callbacks) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        callbacks.forEach((caption, callback) -> rowInline.add(new InlineKeyboardButton().setText(caption).setCallbackData(callback)));

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }
}
