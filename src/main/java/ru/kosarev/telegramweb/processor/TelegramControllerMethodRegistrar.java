package ru.kosarev.telegramweb.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kosarev.telegramweb.aspect.TelegramController;
import ru.kosarev.telegramweb.aspect.TelegramMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TelegramControllerMethodRegistrar implements BeanPostProcessor {

    private final Map<String, Pair<Object, Method>> methodMappings = new HashMap<>();

    @Override
    public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String name) throws BeansException {
        Class<?> beanClass = AopProxyUtils.ultimateTargetClass(bean);

        if (beanClass.isAnnotationPresent(TelegramController.class)) {
            Method[] classMethods = beanClass.getDeclaredMethods();
            for (Method method : classMethods) {
                if (method.isAnnotationPresent(TelegramMapping.class)) {
                    String methodSlug = method.getAnnotation(TelegramMapping.class).value();

                    log.debug("Adding mapping for class: {}, methodSlug: {}", beanClass.getName(), methodSlug);

                    methodMappings.put(methodSlug, Pair.of(bean, method));
                }
            }
        }

        return bean;
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
