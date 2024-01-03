package com.gastro_ukrittya.bot.handler;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Service
public class ValidationService {

    public boolean isTimeCorrect(String time) {
        return time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]");
    }

    public boolean isComplianceWorkSchedule(String time) {
        LocalTime parse = LocalTime.parse(getTime(time));
        return parse.isAfter(LocalTime.parse("09:59")) && parse.isBefore(LocalTime.parse("22:00"));
    }

    public String getTime(String time) {
        return time.matches("^\\d{2}:\\d{2}$")? time : "0" + time;
    }

    public boolean isDateCorrect(String date) {
        return date.matches("^\\d?\\d\\.\\d{2}\\.\\d{4}$") ||
                date.matches("^\\d?\\d\\.\\d{2}$") ||
                date.matches("^\\d?\\d$");
    }

    public boolean isDatePast(String date) {
        LocalDate DATE = LocalDate.parse(getDate(date), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return DATE.isBefore(LocalDate.now());
    }

    public String getDate(String date) {
        if (date.matches("^\\d?\\b$")) {
            return formatSingleDigitDate(date) + "." +
                    formatSingleDigitDate(String.valueOf(LocalDate.now().getMonthValue())) + "." +
                    LocalDate.now().getYear();
        }
        if (date.matches("^\\d{2}.\\d{2}$")) {
            return date + "." + LocalDate.now().getYear();
        }
        if (date.matches("^\\d{2}.\\d{2}.\\d{4}$")) {

        }
//        else {
//            return "0" + date;
//        }
        return date;
    }

    private String formatSingleDigitDate(String date) {
        return date.matches("^\\d")? "0" + date : date;
    }

    public boolean isPhoneNumberCorrect(String phoneNumber) {
        return phoneNumber.matches("^0\\d{9}$") || phoneNumber.matches("^\\+?380\\d{9}$");
    }
}
