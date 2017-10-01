package com.theorangehub.hbdvbot.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TimeAmount {
    public static TimeAmount[] normalize(TimeAmount... amounts) {
        TimeUnit unit = Arrays.stream(amounts).map(TimeAmount::getUnit)
            .min(Comparator.naturalOrder()).orElse(TimeUnit.values()[0]);

        return Arrays.stream(amounts).map(amount -> amount.convertTo(unit)).toArray(TimeAmount[]::new);
    }

    public static TimeAmount sum(TimeAmount... amounts) {
        for (int i = 0; i < amounts.length; i++) {
            amounts[i] = amounts[i].compress();
        }

        TimeAmount[] normal = normalize(amounts);

        long sum = Arrays.stream(normal).mapToLong(TimeAmount::getAmount).sum();

        return new TimeAmount(sum, normal[0].getUnit()).compress();
    }

    private final long amount;
    private final TimeUnit unit;

    public TimeAmount(long amount, TimeUnit unit) {
        this.amount = amount;
        this.unit = Objects.requireNonNull(unit);
    }

    @Override
    public String toString() {
        return amount + " " + unit.toString().toLowerCase();
    }

    public TimeAmount compress() {
        TimeUnit lossless = this.unit;
        TimeUnit[] timeUnits = TimeUnit.values();
        for (int i = lossless.ordinal() + 1; i < timeUnits.length; i++) {
            TimeUnit timeUnit = timeUnits[i];

            //If do back and forth conversion is lossless
            if (unit.convert(timeUnit.convert(amount, unit), timeUnit) == amount) lossless = timeUnit;
            else break;
        }

        return convertTo(lossless);
    }

    public TimeAmount convertTo(TimeUnit newUnit) {
        if (unit.equals(newUnit)) return this;
        return new TimeAmount(newUnit.convert(amount, unit), newUnit);
    }

    public long getAmount() {
        return amount;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void sleep() throws InterruptedException {
        unit.sleep(amount);
    }

    public void timedJoin(Thread thread) throws InterruptedException {
        unit.timedJoin(thread, amount);
    }

    public void timedWait(Object obj) throws InterruptedException {
        unit.timedWait(obj, amount);
    }

    public long toDays() {
        return unit.toDays(amount);
    }

    public long toHours() {
        return unit.toHours(amount);
    }

    public long toMicros() {
        return unit.toMicros(amount);
    }

    public long toMillis() {
        return unit.toMillis(amount);
    }

    public long toMinutes() {
        return unit.toMinutes(amount);
    }

    public long toNanos() {
        return unit.toNanos(amount);
    }

    public long toSeconds() {
        return unit.toSeconds(amount);
    }
}
