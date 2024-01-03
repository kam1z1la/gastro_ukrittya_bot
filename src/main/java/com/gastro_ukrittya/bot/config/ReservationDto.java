package com.gastro_ukrittya.bot.config;

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
    private String date;
    private String time;
    private String numberOfPeople;
    private String name;
    private String phoneNumber;
    private Integer messageId;
    private boolean isReservation;
    private boolean isEditReservation;
    private Client client;

    public ReservationDto toDto(Client client, Reservation reservation) {
        LocalDate date = reservation.getDate();
        String format = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        return ReservationDto.builder()
                .name(client.getFirstName())
                .messageId(reservation.getMessageId())
                .client(client)
                .date(format)
                .time(reservation.getTime().toString())
                .numberOfPeople(reservation.getNumberOfPeople())
                .client(client)
                .build();
    }
}
