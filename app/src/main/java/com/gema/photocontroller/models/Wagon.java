package com.gema.photocontroller.models;

public class Wagon {

    private long number;
    private String code;
    private String name;
    private WagonType type;

    public Wagon(int number, String code, String name, WagonType type) {
        this.number = number;
        this.type = type;
        this.code = code;
        this.name = name;
    }

    public long getNumber() {
        return number;
    }

    public WagonType getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
