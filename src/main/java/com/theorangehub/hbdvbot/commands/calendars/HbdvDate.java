package com.theorangehub.hbdvbot.commands.calendars;

import java.time.LocalDate;
import java.util.Objects;

public class HbdvDate {
    private final HbdvCalendar calendar;
    private final LocalDate internalDate;

    public HbdvDate(LocalDate internalDate, HbdvCalendar calendar) {
        this.internalDate = Objects.requireNonNull(internalDate);
        this.calendar = Objects.requireNonNull(calendar);
    }

    public String format() {
        return format(calendar);
    }

    public String format(HbdvCalendar calendar) {
        return calendar.format(internalDate);
    }

    public HbdvCalendar getCalendar() {
        return calendar;
    }

    public LocalDate getInternalDate() {
        return internalDate;
    }
}
