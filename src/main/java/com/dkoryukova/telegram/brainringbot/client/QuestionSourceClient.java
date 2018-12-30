package com.dkoryukova.telegram.brainringbot.client;

import com.dkoryukova.telegram.brainringbot.model.QuestionPack;

public interface QuestionSourceClient {
    QuestionPack nextPack();
}
