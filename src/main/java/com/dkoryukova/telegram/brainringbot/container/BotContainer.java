package com.dkoryukova.telegram.brainringbot.container;

import com.dkoryukova.telegram.brainringbot.controller.AbstractController;

import java.util.HashMap;
import java.util.Map;

public class BotContainer {
    private Map<String, AbstractController> controllerMap;

    public static BotContainer getInstanse() {
        return Holder.SINGLETON;
    }

    public void addBotController(String path, AbstractController controller) {
        controllerMap.put(path, controller);
    }

    public AbstractController getBotApiMethodController(String path) {
        return controllerMap.get(path);
    }

    private BotContainer() {
        controllerMap = new HashMap<>();
    }

    private static class Holder {
        final static BotContainer SINGLETON = new BotContainer();
    }
}