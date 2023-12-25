package com.gastro_ukrittya.bot.config;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User {
    private Long chatId;
    private String date;
    private String time;
    private String numberOfPeople;
    private String name;
    private String phoneNumber;
    private boolean isReservation;
}
