package ru.kosarev.vpn.telegramweb.processor;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import ru.kosarev.vpn.telegramweb.aspect.TelegramController;
import ru.kosarev.vpn.telegramweb.aspect.TelegramMapping;

import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class TelegramControllerAnnotationProcessor implements BeanPostProcessor {

    private final TelegramControllerMethodRegistrar methodRegistrar;

    @Override
    public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String name) throws BeansException {
        Class<?> beanClass = AopProxyUtils.ultimateTargetClass(bean);

        if (beanClass.isAnnotationPresent(TelegramController.class)) {
            Method[] classMethods = beanClass.getDeclaredMethods();
            for (Method method : classMethods) {
                if (method.isAnnotationPresent(TelegramMapping.class)) {
                    String methodSlug = method.getAnnotation(TelegramMapping.class).value();

                    methodRegistrar.addMethodMapping(methodSlug, bean, method);
                }
            }
        }

        return bean;
    }

}
