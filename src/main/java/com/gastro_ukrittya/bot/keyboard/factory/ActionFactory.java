package com.gastro_ukrittya.bot.keyboard.factory;

import com.gastro_ukrittya.bot.keyboard.KeyboardAbstractFactory;
import com.gastro_ukrittya.bot.keyboard.products.inline.SaveAndCancelInlineKeyboard;
import com.gastro_ukrittya.bot.keyboard.products.reply.CancelReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class ActionFactory implements KeyboardAbstractFactory {
    @Override
    public InlineKeyboardMarkup createInlineKeyboardMarkup() {
        return new SaveAndCancelInlineKeyboard().createInlineKeyboard();
    }

    @Override
    public ReplyKeyboardMarkup createReplyKeyboardMarkup() {
        return new CancelReplyKeyboard().createReplyKeyboard();
    }
}
