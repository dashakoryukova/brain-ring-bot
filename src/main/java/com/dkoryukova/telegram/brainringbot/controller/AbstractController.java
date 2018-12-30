package com.dkoryukova.telegram.brainringbot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractController {
    private final Logger logger = LoggerFactory.getLogger(AbstractController.class);

    private Object bean;
    private Method method;

    public AbstractController(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public abstract boolean validInput(Update update);

    public BotApiMethod process(Update update) {
        if (!validInput(update)) return null;

        try {
            return (BotApiMethod) method.invoke(bean, update);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage());
        }

        return null;
    }
}