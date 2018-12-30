package com.dkoryukova.telegram.brainringbot.processor;

import com.dkoryukova.telegram.brainringbot.annotation.BotController;
import com.dkoryukova.telegram.brainringbot.annotation.BotRequestMapping;
import com.dkoryukova.telegram.brainringbot.container.BotContainer;
import com.dkoryukova.telegram.brainringbot.factory.ControllerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class BotControllerBeanPostProcessor implements BeanPostProcessor {
    private BotContainer container = BotContainer.getInstanse();
    private Map<String, Class> botControllerMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(BotController.class)) {
            botControllerMap.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!botControllerMap.containsKey(beanName)) return bean;

        Arrays.stream(botControllerMap.get(beanName)
                .getMethods())
                .filter(method -> method.isAnnotationPresent(BotRequestMapping.class))
                .forEach((Method method) -> generateController(bean, method));
        return bean;
    }

    private void generateController(Object bean, Method method) {
        BotController botController = bean.getClass().getAnnotation(BotController.class);
        BotRequestMapping botRequestMapping = method.getAnnotation(BotRequestMapping.class);

        String path = botController.value()[0] + botRequestMapping.value()[0];
        container.addBotController(path, ControllerFactory.get(botRequestMapping.method()[0], bean, method));
    }
}