package com.itechart.javalab.library.domain.entity;

import java.util.Arrays;

public enum TimePeriod {

    ONE(1), TWO(2), THREE(3), SIX(6), TWELVE(12);

    private int monthPeriod;

    TimePeriod(int monthPeriod) {
        this.monthPeriod = monthPeriod;
    }

    public int getMonthPeriod() {
        return monthPeriod;
    }

    public void setMonthPeriod(int monthPeriod) {
        this.monthPeriod = monthPeriod;
    }

    public static boolean containsMonthPeriod(int monthPeriod) {
        return Arrays.stream(TimePeriod.values()).anyMatch(x -> x.getMonthPeriod() == monthPeriod);
    }
}
