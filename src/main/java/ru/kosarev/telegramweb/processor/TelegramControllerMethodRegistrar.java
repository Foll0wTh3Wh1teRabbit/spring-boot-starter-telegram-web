package ru.kosarev.telegramweb.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TelegramControllerMethodRegistrar implements BeanPostProcessor {

    private final Map<String, Pair<Object, Method>> methodMappings = new HashMap<>();

    public void registerMethodMapping(String methodName, Object bean, Method method) {
        log.debug("Adding mapping for methodName: {}, beanClass: {}", methodName, bean.getClass().getName());

        methodMappings.put(methodName, Pair.of(bean, method));
    }

    public void callMethodMapping(String methodName, Update update) {
        Pair<Object, Method> controllerMethodPair = methodMappings.get(methodName);

        if (controllerMethodPair == null) {
            log.debug("No controllerMethodPair found for methodName: {}", methodName);

            return;
        }

        Object controller = controllerMethodPair.getLeft();
        Method method = controllerMethodPair.getRight();

        try {
            log.debug("Calling method of class: {}, method: {}", controller.getClass(), methodName);

            method.invoke(controller, update);
        } catch (Exception e) {
            log.debug("An exception occurred during call mapping: ", e);
        }
    }

}
