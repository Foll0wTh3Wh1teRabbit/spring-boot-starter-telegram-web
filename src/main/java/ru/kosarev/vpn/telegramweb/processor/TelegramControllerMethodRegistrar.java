package ru.kosarev.vpn.telegramweb.processor;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TelegramControllerMethodRegistrar {

    private Map<String, Pair<Object, Method>> methodMappings;

    @PostConstruct
    public void init() {
        methodMappings = new HashMap<>();
    }

    public void addMethodMapping(String methodName, Object controller, Method method) {
        methodMappings.put(methodName, Pair.of(controller, method));
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
