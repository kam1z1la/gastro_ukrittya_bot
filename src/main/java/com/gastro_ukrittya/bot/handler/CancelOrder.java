package com.gastro_ukrittya.bot.handler;

import com.gastro_ukrittya.bot.command.StartCommand;
import com.gastro_ukrittya.bot.handler.order.stateMachine.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelOrder implements IBotCommand {
    private final StartCommand startCommand;
    private final Reservation reservation;

    @Override
    public String getCommandIdentifier() {
        return Command.CANCEL_ORDER.getCommand();
    }

    @Override
    public String getDescription() {
        return "The user canceled the reservation";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        reservation.getStates().remove(reservation.findUserByChatId(message.getChatId()));
        startCommand.createMainMenu(absSender, message.getChat(), "Головне меню");
    }
}
