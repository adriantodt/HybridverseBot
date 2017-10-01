package com.theorangehub.hbdvbot.commands.calendars.impl;

import com.theorangehub.hbdvbot.commands.calendars.HbdvCalendar;
import com.theorangehub.hbdvbot.commands.calendars.HbdvDate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public class ApexCalendar implements HbdvCalendar {
    @Override
    public HbdvDate of(int year, int month, int day) {
        return new HbdvDate(LocalDate.of(year - 3001, month, day), this);
    }

    @Override
    public String format(LocalDate date) {
        int year = date.getYear() + 3001;

        return String.format("%s, %d° dia do %s do ano %d",
            format(date.getDayOfWeek()), date.getDayOfMonth(), format(date.getMonth()), year
        );
    }

    public String format(DayOfWeek weekday) {
        int v = weekday.getValue();
        switch (v) {
            case 1:
                return "Luácio";
            case 2:
                return "Márcio";
            case 3:
                return "Mercurácio";
            case 4:
                return "Jupitácio";
            case 5:
                return "Venácio";
            case 6:
                return "Saturnácio";
            case 7:
                return "Solácio";
        }

        throw new IllegalStateException("weekday " + v);
    }

    public String format(Month month) {
        int v = month.getValue();
        switch (v) {
            case 1:
                return "Primeiro Ciclo";
            case 2:
                return "Segundo Ciclo";
            case 3:
                return "Terceiro Ciclo";
            case 4:
                return "Quarto Ciclo";
            case 5:
                return "Quinto Ciclo";
            case 6:
                return "Sexto Ciclo";
            case 7:
                return "Sétimo Ciclo";
            case 8:
                return "Oitavo Ciclo";
            case 9:
                return "Nono Ciclo";
            case 10:
                return "Décimo Ciclo";
            case 11:
                return "Penúltimo Ciclo";
            case 12:
                return "Último Ciclo";
        }

        throw new IllegalStateException("month " + v);
    }
}
