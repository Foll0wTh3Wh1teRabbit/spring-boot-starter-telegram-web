package ru.kosarev.vpn.telegramweb.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kosarev.vpn.telegramweb.processor.TelegramControllerMethodRegistrar;

@Component
@RequiredArgsConstructor
public class TelegramBotTemplate implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramControllerMethodRegistrar methodRegistrar;

    @Value("${spring.telegram.bot-token}")
    private String botToken;

    @Override
    public void consume(Update update) {
        String methodName = update.getMessage().getText();

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
