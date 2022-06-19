package com.hankoh.scheduleapp.controller;

import java.time.LocalDate;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;


public class TimeUtil {
    public static boolean isCurrentWeek(LocalDate appointmentTime) {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(previousOrSame(SUNDAY));
        LocalDate weekEnd = today.with(nextOrSame(SATURDAY));

        if (appointmentTime.compareTo(weekEnd) <= 0 &&
            appointmentTime.compareTo(weekStart) >= 0) {
            return true;
        }
        return false;
    }
}
