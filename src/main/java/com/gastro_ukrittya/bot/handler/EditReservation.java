package com.gastro_ukrittya.bot.handler;

import com.gastro_ukrittya.bot.handler.state.edit_reservation.EditContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.gastro_ukrittya.bot.config.Command.EDIT_RESERVE;


@Slf4j
@Service
@RequiredArgsConstructor
public class EditReservation implements IBotCommand {
    private final EditContext context;

    @Override
    public String getCommandIdentifier() {
        return EDIT_RESERVE.getCommand();
    }

    @Override
    public String getDescription() {
        return "The user canceled the reservation";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        context.doAction(absSender, message);
    }
}
