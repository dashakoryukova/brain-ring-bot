package com.dkoryukova.telegram.brainringbot.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Game {
    private Question question;
    private Integer totalPlayed;
    private Integer totalWon;
}
