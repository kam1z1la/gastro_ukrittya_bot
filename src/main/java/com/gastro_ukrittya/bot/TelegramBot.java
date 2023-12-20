package com.gastro_ukrittya.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.menubutton.SetChatMenuButton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.menubutton.MenuButtonWebApp;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig bot;

    public TelegramBot(BotConfig bot) {
        super(bot.getToken());
        this.bot = bot;
    }

    @Override
    public String getBotUsername() {
        return bot.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        createMenuButton(update);
        sendReplyKeyboardMessage(update.getMessage().getChatId());
    }

    public void sendReplyKeyboardMessage(long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("\uD83D\uDCDE  Контакти");
        row.add("\uD83C\uDFAD  Стендап");
        keyboard.add(row);

        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add("\uD83C\uDF74  Забронювати столик");
        keyboard.add(secondRow);

        keyboardMarkup.setKeyboard(keyboard);

        try {
            execute(SendMessage.builder()
                    .chatId(chatId)
                    .text("Your message text")
                    .replyMarkup(keyboardMarkup)
                    .build()
            );
        } catch (TelegramApiException e) {
            log.error("[Failed to send message with reply keyboard]: {}", e.getMessage());
        }
    }

    private void createMenuButton(Update update) {
        SetChatMenuButton setChatMenuButton = new SetChatMenuButton();
        setChatMenuButton.setChatId(update.getMessage().getChatId());
        setChatMenuButton.setMenuButton(
                MenuButtonWebApp.builder()
                        .text("Меню")
                        .webAppInfo(WebAppInfo.builder()
                                .url("https://gastroukryttia.ps.me/")
                                .build())
                        .build());

        //todo create method
        try {
            execute(setChatMenuButton);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
