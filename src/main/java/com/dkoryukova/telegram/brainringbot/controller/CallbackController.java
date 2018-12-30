package com.dkoryukova.telegram.brainringbot.controller;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;

public abstract class CallbackController extends AbstractController {

    protected CallbackController(Object bean, Method method) {
        super(bean, method);
    }

    @Override
    public boolean validInput(Update update) {
        return update != null && update.hasCallbackQuery() && !update.getCallbackQuery().getData().isEmpty();
    }
}
