package com.gastro_ukrittya.bot.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.menubutton.SetChatMenuButton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.menubutton.MenuButtonWebApp;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Component
public class StartCommand extends BotCommand {

    public StartCommand() {
        super("start", "start using bot\n");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        createMainMenu(absSender, chat, "\uD83D\uDC4B");
    }

    public void createMainMenu(AbsSender absSender, Chat chat, String text) {
        try {
            absSender.execute(SendMessage.builder()
                    .chatId(chat.getId().toString())
                    .text(text)
                    .replyMarkup(createReplyKeyboardMarkup())
                    .build());
            absSender.execute(createMenuButton(chat.getId().toString()));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public SetChatMenuButton createMenuButton(String chatId) {
        return SetChatMenuButton.builder()
                .chatId(chatId)
                .menuButton(MenuButtonWebApp.builder()
                        .text("Меню")
                        .webAppInfo(WebAppInfo.builder()
                                .url("https://gastroukryttia.ps.me/")
                                .build())
                        .build())
                .build();
    }

    public ReplyKeyboardMarkup createReplyKeyboardMarkup() {
        return ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .keyboardRow(new KeyboardRow(
                        List.of(
                                new KeyboardButton("\uD83D\uDCDE  Контакти"),
                                new KeyboardButton("\uD83C\uDFAD  Стендап")
                        )
                ))
                .keyboardRow(new KeyboardRow(
                        List.of(new KeyboardButton("\uD83E\uDD42 Забронювати столик"))
                ))
                .build();
    }
}
