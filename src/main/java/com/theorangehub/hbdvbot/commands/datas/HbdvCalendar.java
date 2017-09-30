package com.theorangehub.hbdvbot.commands.datas;

import java.time.LocalDate;

public interface HbdvCalendar {
    HbdvDate of(int year, int month, int day);

    String format(LocalDate internalDate);
}
