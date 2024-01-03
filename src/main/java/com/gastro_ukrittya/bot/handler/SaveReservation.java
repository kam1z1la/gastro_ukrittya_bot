package com.gastro_ukrittya.bot.handler;

import com.gastro_ukrittya.bot.config.ReservationDto;
import com.gastro_ukrittya.bot.db.client.ClientService;
import com.gastro_ukrittya.bot.db.reservation.Reservation;
import com.gastro_ukrittya.bot.db.reservation.ReservationService;
import com.gastro_ukrittya.bot.keyboard.KeyboardProvider;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.StringJoiner;

import static com.gastro_ukrittya.bot.config.Command.BOOKED;
import static com.gastro_ukrittya.bot.keyboard.Keyboard.MAIN;

@Slf4j
@Setter
@Component
@RequiredArgsConstructor
public class SaveReservation implements IBotCommand, Notification {
    private final ReservationService reservationService;
    private final ClientService clientService;
    private final KeyboardProvider keyboard;
    private ReservationDto reservationDto;

    @Override
    public String getCommandIdentifier() {
        return BOOKED.getCommand();
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        try {
            Long reservationId = save(reservationDto);
//            -1001939670189L
            Message execute = absSender.execute(SendMessage.builder()
                    .chatId(565537140L)
                    .replyMarkup(keyboard.getKeyboard(MAIN).createInlineKeyboardMarkup())
                    .text(getNotification())
                    .parseMode(ParseMode.MARKDOWN)
                    .build());

            Reservation reservation = reservationService.findReservationById(reservationId);
            reservation.setMessageId(execute.getMessageId());
            reservationService.updateReservation(reservation);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private Long save(ReservationDto reservationDto) {
        Long clientId = clientService.addClient(reservationDto);
        reservationDto.setClient(clientService.findClientById(clientId));

        Long id = reservationService.addReservation(reservationDto);
        reservationDto.setNumberReservation(id);
        return id;

    }

    @Override
    public String getNotification() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(String.format("Бронь № %d\n", reservationDto.getNumberReservation()));
        joiner.add(String.format("Ім'я замовника:\t %s", reservationDto.getName()));
        joiner.add(String.format("Дата:\t %s", reservationDto.getDate()));
        joiner.add(String.format("Час:\t %s", reservationDto.getTime()));
        joiner.add(String.format("Кількість людей:\t %s", reservationDto.getNumberOfPeople()));
        joiner.add(String.format("Номер телефону:\t `%s`", reservationDto.getPhoneNumber()));
        return joiner.toString();
    }
}
