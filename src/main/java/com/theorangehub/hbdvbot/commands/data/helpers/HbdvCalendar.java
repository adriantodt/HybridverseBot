package com.theorangehub.hbdvbot.commands.data.helpers;

import java.time.LocalDate;

public interface HbdvCalendar {
    HbdvDate of(int year, int month, int day);

    String format(LocalDate internalDate);
}
