package com.dkoryukova.telegram.brainringbot.factory;

import com.dkoryukova.telegram.brainringbot.controller.AbstractController;
import com.dkoryukova.telegram.brainringbot.controller.CallbackController;
import com.dkoryukova.telegram.brainringbot.controller.MessageController;
import com.dkoryukova.telegram.brainringbot.model.BotRequestMethod;

import java.lang.reflect.Method;

public class ControllerFactory {

    public static AbstractController get(BotRequestMethod botRequestMethod, Object bean, Method method) {
        switch (botRequestMethod) {
            case MESSAGE:
                return new MessageController(bean, method) {};
            case CALLBACK:
                return new CallbackController(bean, method) {};
        }
        return null;
    }
}
