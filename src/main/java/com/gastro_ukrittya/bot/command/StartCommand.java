package com.gastro_ukrittya.bot.command;

import com.gastro_ukrittya.bot.keyboard.KeyboardProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.menubutton.SetChatMenuButton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.menubutton.MenuButtonWebApp;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.gastro_ukrittya.bot.keyboard.Keyboard.MAIN;

@Slf4j
@Component
public class StartCommand extends BotCommand {
    private final KeyboardProvider keyboard;

    public StartCommand(KeyboardProvider keyboard) {
        super("start", "start using bot\n");
        this.keyboard = keyboard;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        createStartInterface(absSender, chat, "\uD83D\uDC4B");
    }

    public void createStartInterface(AbsSender absSender, Chat chat, String text) {
        try {
            absSender.execute(SendMessage.builder()
                    .chatId(chat.getId())
                    .text(text)
                    .replyMarkup(keyboard.getKeyboard(MAIN).createReplyKeyboardMarkup())
                    .build());
            absSender.execute(createMenuButton(chat.getId().toString()));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private SetChatMenuButton createMenuButton(String chatId) {
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
}
