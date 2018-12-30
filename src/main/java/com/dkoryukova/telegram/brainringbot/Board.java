package com.dkoryukova.telegram.brainringbot;

import com.dkoryukova.telegram.brainringbot.model.Game;
import com.dkoryukova.telegram.brainringbot.model.Question;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private static Map<Long, Game> BOARD = new HashMap<>();

    public static void startRound(Long chatId, Question question) {
        BOARD.putIfAbsent(chatId, Game.builder()
                .totalPlayed(0)
                .totalWon(0).build());
        BOARD.get(chatId).setQuestion(question);
    }

    public static void completeRound(Long chatId, Boolean correct) {
        Game game = BOARD.get(chatId);
        game.setTotalPlayed(game.getTotalPlayed() + 1);
        if (correct) {
            game.setTotalWon(game.getTotalWon() + 1);
        }
        game.setQuestion(null);
    }

    public static Question getQuestion(Long chatId) {
        return BOARD.get(chatId).getQuestion();
    }

    public static boolean isPlaying(Long chatId) {
        return BOARD.containsKey(chatId) && BOARD.get(chatId).getQuestion() != null;
    }

    public static Integer getTotalPlayed(Long chatId) {
        return BOARD.get(chatId).getTotalPlayed();
    }

    public static Integer getTotalWon(Long chatId) {
        return BOARD.get(chatId).getTotalWon();
    }

    public static void appelation(Long chatId) {
        Game game = BOARD.get(chatId);
        game.setTotalWon(game.getTotalWon() + 1);
    }
}
