package com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import com.gastro_ukrittya.bot.db.reservation.ReservationService;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class DeleteEditState implements EditState {
    @Override
    public void handle(AbsSender absSender, Message message, EditContext context) {
        ReservationDto reservationDto = context.getReservationDto();
        deleteReservation(reservationDto.getReservationId(), context);
        deleteMessage(absSender, message, reservationDto);
        nextState(context);
    }

    private void deleteReservation(long reservationId, EditContext context) {
        ReservationService reservationService = context.getReservationService();
        reservationService.deleteReservation(reservationId);
    }

    private void deleteMessage(AbsSender absSender, Message message, ReservationDto reservationDto) {
        try {
            absSender.execute(
                    DeleteMessage.builder()
                            .chatId(message.getChatId())
                            .messageId(reservationDto.getMessageId())
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void nextState(EditContext context) {
        context.setState(null);
        context.setReservationDto(null);
        context.getHistory().getMementos().clear();
    }
}
