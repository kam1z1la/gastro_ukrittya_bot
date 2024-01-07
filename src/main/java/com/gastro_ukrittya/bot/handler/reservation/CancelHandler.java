package com.gastro_ukrittya.bot.handler.reservation;

import com.gastro_ukrittya.bot.command.StartCommand;
import com.gastro_ukrittya.bot.handler.reservation.state.reservation.Context;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.gastro_ukrittya.bot.config.Command.CANCEL_RESERVATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelHandler implements IBotCommand {
    private final StartCommand startCommand;
    private final Context context;

    @Override
    public String getCommandIdentifier() {
        return CANCEL_RESERVATION.getCommand();
    }

    @Override
    public String getDescription() {
        return "The user canceled the reservation";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        context.deleteUserByChatId(message.getChatId());
        startCommand.createStartInterface(absSender, message.getChat(), "Головне меню");
    }
}
