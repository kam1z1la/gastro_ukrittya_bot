package com.gastro_ukrittya.bot.keyboard.factory;

import com.gastro_ukrittya.bot.keyboard.KeyboardAbstractFactory;
import com.gastro_ukrittya.bot.keyboard.products.inline.ReservationEditorInlineKeyboard;
import com.gastro_ukrittya.bot.keyboard.products.reply.ContactReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class AdditionalFeatures implements KeyboardAbstractFactory {
    @Override
    public InlineKeyboardMarkup createInlineKeyboardMarkup() {
        return new ReservationEditorInlineKeyboard().createInlineKeyboard();
    }

    @Override
    public ReplyKeyboardMarkup createReplyKeyboardMarkup() {
        return new ContactReplyKeyboard().createReplyKeyboard();
    }
}
