package com.gema.photocontroller.models;

public class WagonType {

    private long id;
    private String code;


    public WagonType(long id, String code) {
        this.id = id;
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }
}
