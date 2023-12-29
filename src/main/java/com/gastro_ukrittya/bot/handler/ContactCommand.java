package com.gastro_ukrittya.bot.handler;

import com.gastro_ukrittya.bot.config.ContactConfig;
import com.gastro_ukrittya.bot.keyboard.ReplyMarkupFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVenue;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.StringJoiner;

import static com.gastro_ukrittya.bot.handler.Command.CONTACT;
import static com.gastro_ukrittya.bot.keyboard.Event.MAIN;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactCommand implements IBotCommand, Notification {
    private final ReplyMarkupFactory keyboard;
    private final ContactConfig contact;

    @Override
    public String getCommandIdentifier() {
        return CONTACT.getCommand();
    }

    @Override
    public String getDescription() {
        return "User views contact information";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        try {
            Message locationMessage = absSender.execute(SendVenue.builder()
                    .chatId(message.getChatId())
                    .latitude(contact.getLatitude())
                    .longitude(contact.getLongitude())
                    .title("Gastro Укриття")
                    .address(String.format("\uD83D\uDCCD %s", contact.getAddress()))
                    .googlePlaceId(contact.getGooglePlaceId())
                    .build());

            absSender.execute(SendMessage.builder()
                    .chatId(message.getChatId())
                    .text(getNotification())
                    .parseMode(ParseMode.MARKDOWN)
                    .disableWebPagePreview(true)
                    .replyToMessageId(locationMessage.getMessageId())
                    .replyMarkup(keyboard.getKeyboard(MAIN))
                    .build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getNotification() {
        StringJoiner joiner = new StringJoiner("\n\n");
        joiner.add(String.format("\uD83D\uDCDE `%s`", contact.getPhoneNumber()));
        joiner.add(String.format("✉ `%s`", contact.getMail()));
        joiner.add(String.format("[Фейсбук](%s) | [Інстаграм](%s)", contact.getFacebook(), contact.getInstagram()));
        return joiner.toString();
    }
}
