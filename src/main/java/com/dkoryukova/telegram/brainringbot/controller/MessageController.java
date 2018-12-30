package com.dkoryukova.telegram.brainringbot.controller;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;

public abstract class MessageController extends AbstractController {

    protected MessageController(Object bean, Method method) {
        super(bean, method);
    }

    @Override
    public boolean validInput(Update update) {
        return update != null && update.hasMessage() && update.getMessage().hasText();
    }

}
