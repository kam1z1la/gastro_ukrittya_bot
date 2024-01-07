package com.gastro_ukrittya.bot.handler.reservation;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;


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
        return date.matches("^\\d{1,2}\\.\\d{2}\\.\\d{4}$") ||
                date.matches("^\\d{1,2}\\.\\d{2}$") ||
                date.matches("^\\d{1,2}$");
    }

    public boolean isCurrentDate(String date) {
        LocalDate fullDate = LocalDate.parse(getDate(date), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return !fullDate.isBefore(LocalDate.now());
    }

    public String getDate(String date) {
        if (date.matches("^\\d{1,2}$")) {
            return formatSingleDigitDate(date) + "." +
                    formatSingleDigitDate(String.valueOf(LocalDate.now().getMonthValue())) + "." +
                    LocalDate.now().getYear();
        }
        if (date.matches("^\\d{1,2}.\\d{2}$")) {
            return formatSingleDigitDate(date) + "." + LocalDate.now().getYear();
        }
        return date;
    }

    private String formatSingleDigitDate(String date) {
        return date.matches("^\\d")? "0" + date : date;
    }

    public boolean isPhoneNumberCorrect(String phoneNumber) {
        String number = replaceAllSpace(phoneNumber);
        return number.matches("^0\\d{9}$") || number.matches("^\\+?380\\d{9}$");
    }

    public String getPhoneNumber(String phoneNumber) {
        String number = replaceAllSpace(phoneNumber);
        if (number.matches("^\\+?380.*")) {
            return Arrays.stream(number.split(""))
                    .dropWhile(chars -> !chars.equals("0"))
                    .collect(Collectors.joining());
        }
        return number;
    }

    private String replaceAllSpace(String text) {
        return text.replaceAll("\\s+", "");
    }
}
