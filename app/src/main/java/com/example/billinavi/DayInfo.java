package com.example.billinavi;


public class DayInfo {
    private String day, month, year;
    private boolean inMonth;

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDay() {
        return day;
    }

    public boolean isInMonth(){
        return inMonth;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setInMonth(boolean inMonth) {
        this.inMonth = inMonth;
    }
}