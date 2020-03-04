package com.deathparade.telegrambot.domain;

import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@Entity
public class ExchangeRate {

    @Id
    private String base;
    private String date;
    // for test
    private String lastUpdate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()).toString();
    private double value;

    public ExchangeRate() {
    }

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public double getValue() {
        return value;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Base: %s\nDate: %s\nLast update: %s\nValue: %f", base, date, lastUpdate, value);
    }
}