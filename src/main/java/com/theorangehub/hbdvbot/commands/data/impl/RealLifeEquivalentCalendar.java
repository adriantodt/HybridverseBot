package com.theorangehub.hbdvbot.commands.data.impl;

import com.theorangehub.hbdvbot.commands.data.HbdvCalendar;
import com.theorangehub.hbdvbot.commands.data.HbdvDate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public class RealLifeEquivalentCalendar implements HbdvCalendar {
    @Override
    public HbdvDate of(int year, int month, int day) {
        return new HbdvDate(LocalDate.of(year - 1, month, day).minusMonths(3).minusDays(21), this);
    }

    @Override
    public String format(LocalDate date) {
        LocalDate date1 = date.plusYears(1).plusMonths(3).plusDays(21);

        return String.format("%s, %d de %s de %d",
            format(date.getDayOfWeek()), date1.getDayOfMonth(), format(date1.getMonth()), date1.getYear()
        );
    }

    public String format(DayOfWeek weekday) {
        int v = weekday.getValue();
        switch (v) {
            case 1:
                return "Segunda-feira";
            case 2:
                return "Terça-feira";
            case 3:
                return "Quarta-feira";
            case 4:
                return "Quinta-feira";
            case 5:
                return "Sexta-feira";
            case 6:
                return "Sábado";
            case 7:
                return "Domingo";
        }

        throw new IllegalStateException("weekday " + v);
    }

    public String format(Month month) {
        int v = month.getValue();
        switch (v) {
            case 1:
                return "Janeiro";
            case 2:
                return "Fevereiro";
            case 3:
                return "Março";
            case 4:
                return "Abril";
            case 5:
                return "Maio";
            case 6:
                return "Junho";
            case 7:
                return "Julho";
            case 8:
                return "Agosto";
            case 9:
                return "Setembro";
            case 10:
                return "Outubro";
            case 11:
                return "Novembro";
            case 12:
                return "Dezembro";
        }

        throw new IllegalStateException("month " + v);
    }
}
