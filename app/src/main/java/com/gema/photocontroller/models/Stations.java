package com.gema.photocontroller.models;

import android.util.Log;

import org.json.JSONObject;

public class Stations {

    private String id;
    private String name;

    public Stations(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Stations(JSONObject jsonObject) throws Exception {
        this.id = jsonObject.getString("id");
        this.name = jsonObject.getString("name");
    }

    public JSONObject getJSON() {
        JSONObject problemJSON = new JSONObject();
        try {
            problemJSON.put("id", this.id);
            problemJSON.put("name", this.name);
        } catch (Exception e) {
            Log.e("GETJSON STATIONS", "Ошибка сериализации объекта station в JSON");
        }
        return problemJSON;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

}
