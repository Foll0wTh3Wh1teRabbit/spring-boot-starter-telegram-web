package ru.kosarev.telegramweb.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kosarev.telegramweb.processor.TelegramControllerMethodRegistrar;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "spring.telegram", name = "bot-token")
@RequiredArgsConstructor
public class TelegramBotTemplate implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramControllerMethodRegistrar methodRegistrar;

    @Value("${spring.telegram.bot-token}")
    private String botToken;

    @Override
    public void consume(Update update) {
        String methodName = update.getMessage().getText();

        log.debug("Received call for methodName: {}", methodName);

        methodRegistrar.callMethodMapping(methodName, update);
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

}
