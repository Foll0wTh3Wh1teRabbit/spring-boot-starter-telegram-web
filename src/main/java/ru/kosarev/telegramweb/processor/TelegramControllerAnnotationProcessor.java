package ru.kosarev.telegramweb.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import ru.kosarev.telegramweb.aspect.TelegramController;
import ru.kosarev.telegramweb.aspect.TelegramMapping;

import java.lang.reflect.Method;

@Slf4j
@RequiredArgsConstructor
public class TelegramControllerAnnotationProcessor implements BeanPostProcessor {

    private final TelegramControllerMethodRegistrar methodRegistrar;

    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, @NotNull String name) throws BeansException {
        Class<?> beanClass = AopProxyUtils.ultimateTargetClass(bean);

        if (beanClass.isAnnotationPresent(TelegramController.class)) {
            Method[] classMethods = beanClass.getDeclaredMethods();
            for (Method method : classMethods) {
                if (method.isAnnotationPresent(TelegramMapping.class)) {
                    String methodSlug = method.getAnnotation(TelegramMapping.class).value();

                    log.debug("Adding mapping for class: {}, methodSlug: {}", beanClass.getName(), methodSlug);

                    methodRegistrar.registerMethodMapping(methodSlug, bean, method);
                }
            }
        }

        return bean;
    }

}
