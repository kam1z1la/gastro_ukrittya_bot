package com.gastro_ukrittya.bot.keyboard.factory;

import com.gastro_ukrittya.bot.keyboard.KeyboardAbstractFactory;
import com.gastro_ukrittya.bot.keyboard.products.inline.MainInlineMenu;
import com.gastro_ukrittya.bot.keyboard.products.reply.MainReplyMenu;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class MainMenuFactory implements KeyboardAbstractFactory {
    @Override
    public InlineKeyboardMarkup createInlineKeyboardMarkup() {
        return new MainInlineMenu().createInlineKeyboard();
    }

    @Override
    public ReplyKeyboardMarkup createReplyKeyboardMarkup() {
        return new MainReplyMenu().createReplyKeyboard();
    }
}
