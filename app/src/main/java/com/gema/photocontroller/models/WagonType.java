package com.gema.photocontroller.models;

public class WagonType {

    private long id;
    private String code;
    private String name;


    public WagonType(long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
