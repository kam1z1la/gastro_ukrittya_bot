package com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import com.gastro_ukrittya.bot.db.client.Client;
import com.gastro_ukrittya.bot.db.client.ClientService;
import com.gastro_ukrittya.bot.db.reservation.Reservation;
import com.gastro_ukrittya.bot.db.reservation.ReservationService;
import com.gastro_ukrittya.bot.dto.ReservationHistory;
import com.gastro_ukrittya.bot.handler.reservation.ValidationService;
import com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation.date.DateEditState;
import com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation.name.NameEditState;
import com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation.number_of_people.NumberOfPeopleEditState;
import com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation.phone_number.PhoneNumberEditState;
import com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation.time.TimeEditState;
import com.gastro_ukrittya.bot.keyboard.KeyboardProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.StringJoiner;

@Setter
@Getter
@Service
@RequiredArgsConstructor
public class EditContext {
    private final ValidationService validation;
    private final ReservationService reservationService;
    private final ClientService clientService;
    private final KeyboardProvider keyboard;
    private ReservationDto reservationDto;
    private ReservationHistory history;
    private EditState state;

    public void handle(AbsSender absSender, Message message, String callbackData) {
        getReservation(message);
        setEditStatus(callbackData);
        state.handle(absSender, message, this);
    }

    private void getReservation(Message message) {
        if (reservationDto == null) {
            Reservation reservation = reservationService.findReservationByMessageId(message.getMessageId());
            Client client = reservation.getClient();
            reservationDto = ReservationDto.toDto(client, reservation);
            createMemento(reservationDto);
        }
    }

    private void createMemento(ReservationDto dto) {
        history = new ReservationHistory(dto);
        ReservationDto.ReservationMemento save = reservationDto.save();
        history.addMemento(save);
    }

    private void setEditStatus(String callbackData) {
        switch (callbackData) {
            case "edit" -> this.setState(new StartEditState());
            case "name" -> this.setState(new NameEditState());
            case "date" -> this.setState(new DateEditState());
            case "time" -> this.setState(new TimeEditState());
            case "numberOfPeople" -> this.setState(new NumberOfPeopleEditState());
            case "phoneNumber" -> this.setState(new PhoneNumberEditState());
            case "back" -> this.setState(new BackEditState());
            case "save" -> this.setState(new SaveEditState());
            case "cancel" -> this.setState(new CancelEditState());
            case "delete-confirm" -> this.setState(new DeletionConfirmationState());
            case "delete" -> this.setState(new DeleteEditState());
        }
    }

    public String getNotification(ReservationDto reservationDto) {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(String.format("Бронь № %d\n", reservationDto.getReservationId()));
        joiner.add(String.format("Ім'я замовника:\t %s", reservationDto.getName()));
        joiner.add(String.format("Дата:\t %s", reservationDto.getDate()));
        joiner.add(String.format("Час:\t %s", reservationDto.getTime()));
        joiner.add(String.format("Кількість людей:\t %s", reservationDto.getNumberOfPeople()));
        joiner.add(String.format("Номер телефону:\t `%s`", reservationDto.getPhoneNumber()));
        return joiner.toString();
    }
}
