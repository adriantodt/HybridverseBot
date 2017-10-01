package com.theorangehub.hbdvbot.commands.calendars;

import java.time.LocalDate;

public interface HbdvCalendar {
    HbdvDate of(int year, int month, int day);

    String format(LocalDate internalDate);
}
