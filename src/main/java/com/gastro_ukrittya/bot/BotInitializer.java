package com.gastro_ukrittya.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class BotInitializer {
//    private final BotConfig config;
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void init() {
//        try {
//            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//            telegramBotsApi.registerBot(new TelegramBot(config));
//        } catch (TelegramApiException e) {
//            log.error("Error when starting bot ", new RuntimeException(e));
//        }
//    }
//}
