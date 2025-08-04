package ru.kosarev.telegramweb.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kosarev.telegramweb.bot.TelegramBotTemplate;
import ru.kosarev.telegramweb.processor.TelegramControllerAnnotationProcessor;
import ru.kosarev.telegramweb.processor.TelegramControllerMethodRegistrar;

@Configuration
public class TelegramWebConfiguration {

    @Value("${spring.telegram.bot-token}")
    private String botToken;

    @Bean
    public TelegramBotTemplate telegramBotTemplate() {
        return new TelegramBotTemplate(botToken, methodRegistrar());
    }

    @Bean
    public static TelegramControllerAnnotationProcessor telegramControllerAnnotationProcessor() {
        return new TelegramControllerAnnotationProcessor(methodRegistrar());
    }

    @Bean
    public static TelegramControllerMethodRegistrar methodRegistrar() {
        return new TelegramControllerMethodRegistrar();
    }

}
