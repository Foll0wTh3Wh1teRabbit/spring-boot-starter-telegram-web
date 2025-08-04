package ru.kosarev.telegramweb.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kosarev.telegramweb.processor.TelegramControllerMethodRegistrar;

@Slf4j
@RequiredArgsConstructor
public class TelegramBotTemplate implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final String botToken;

    private final TelegramControllerMethodRegistrar methodRegistrar;

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
