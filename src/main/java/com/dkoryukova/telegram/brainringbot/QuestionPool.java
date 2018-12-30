package com.dkoryukova.telegram.brainringbot;

import com.dkoryukova.telegram.brainringbot.client.QuestionSourceClient;
import com.dkoryukova.telegram.brainringbot.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
public class QuestionPool {
    @Autowired
    private QuestionSourceClient client;

    private LinkedList<Question> pool = new LinkedList<>();

    public Question getQuestion() {
        if (pool.isEmpty()) {
            pool = client.nextPack().getQuestions();
        }
        return pool.poll();
    }
}
