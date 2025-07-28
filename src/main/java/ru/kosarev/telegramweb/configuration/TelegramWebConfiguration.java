package ru.kosarev.telegramweb.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kosarev.telegramweb.processor.TelegramControllerAnnotationProcessor;
import ru.kosarev.telegramweb.processor.TelegramControllerMethodRegistrar;

@Configuration
public class TelegramWebConfiguration {

    @Bean
    public TelegramControllerAnnotationProcessor telegramControllerAnnotationProcessor() {
        return new TelegramControllerAnnotationProcessor(methodRegistrar());
    }

    @Bean
    public TelegramControllerMethodRegistrar methodRegistrar() {
        return new TelegramControllerMethodRegistrar();
    }

}
