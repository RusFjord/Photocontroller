package com.gema.photocontroller.models;

public class Wagon {

    private int number;
    private WagonType type;

    public Wagon(int number, WagonType type) {
        this.number = number;
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public WagonType getType() {
        return type;
    }
}
