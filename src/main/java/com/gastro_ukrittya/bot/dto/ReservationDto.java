package com.gastro_ukrittya.bot.dto;

import com.gastro_ukrittya.bot.db.client.Client;
import com.gastro_ukrittya.bot.db.reservation.Reservation;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ReservationDto {
    private Long numberReservation;
    private Long chatId;
    private Long clientId;
    private Long reservationId;
    private String date;
    private String time;
    private String numberOfPeople;
    private String name;
    private String phoneNumber;
    private Integer messageId;
    private boolean isReservation;
    private Client client;

    public static ReservationDto toDto(Client client, Reservation reservation) {
        return ReservationDto.builder()
                .chatId(client.getId())
                .reservationId(reservation.getId())
                .name(client.getFirstName())
                .messageId(reservation.getMessageId())
                .date(conversionToFormat(reservation.getDate()))
                .time(reservation.getTime().toString())
                .numberOfPeople(reservation.getNumberOfPeople())
                .phoneNumber(client.getPhoneNumber())
                .client(client)
                .build();
    }

    private static String conversionToFormat(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Getter
    @Setter
    @ToString
    public static class ReservationMemento {
        private final Long numberReservation;
        private final Long chatId;
        private final Long clientId;
        private final Long reservationId;
        private final String date;
        private final String time;
        private final String numberOfPeople;
        private final String name;
        private final String phoneNumber;
        private final Integer messageId;
        private final boolean isReservation;
        private final Client client;

        private ReservationMemento(ReservationDto dto) {
            this.numberReservation = dto.numberReservation;
            this.chatId = dto.chatId;
            this.clientId = dto.clientId;
            this.reservationId = dto.reservationId;
            this.date = dto.date;
            this.time = dto.time;
            this.numberOfPeople = dto.numberOfPeople;
            this.name = dto.name;
            this.phoneNumber = dto.phoneNumber;
            this.messageId = dto.messageId;
            this.isReservation = dto.isReservation;
            this.client = dto.client;
        }
    }

    public ReservationMemento save() {
        return new ReservationMemento(this);
    }

    public ReservationDto restore(ReservationMemento memento) {
       return ReservationDto.builder()
                .chatId(memento.getChatId())
                .reservationId(memento.getReservationId())
                .name(memento.getName())
                .messageId(memento.getMessageId())
                .date(memento.getDate())
                .time(memento.getTime())
                .numberOfPeople(memento.getNumberOfPeople())
                .phoneNumber(memento.getPhoneNumber())
                .client(memento.getClient())
                .build();
    }
}
