package com.theorangehub.hbdvbot.commands.datas.calendars;

import com.theorangehub.hbdvbot.commands.datas.HbdvDate;

import java.time.LocalDate;

public class AfterPlagueCalendar extends ApexCalendar {
    @Override
    public HbdvDate of(int year, int month, int day) {
        return super.of(year + 5007, month, day);
    }
    @Override
    public String format(LocalDate date) {
        int year = date.getYear() + 3001;

        return String.format("%s, %d° dia do %s do %s",
            format(date.getDayOfWeek()), date.getDayOfMonth(), format(date.getMonth()), formatYearPraga(year)
        );
    }

    public String formatYearPraga(int year) {
        if (year < 5007) {
            return 5007 - year + "° ano antes da praga";
        }

        if (year > 5007) {
            return year - 5007 + "° ano depois da praga";
        }

        return "ano da praga";
    }
}
