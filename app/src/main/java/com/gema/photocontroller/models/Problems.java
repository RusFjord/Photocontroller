package com.gema.photocontroller.models;

import android.util.Log;

import org.json.JSONObject;

public class Problems {

    private String id;
    private String name;

    public Problems(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Problems(JSONObject jsonObject) throws Exception {
        this.id = jsonObject.getString("id");
        this.name = jsonObject.getString("name");
    }

    public JSONObject getJSON() {
        JSONObject problemJSON = new JSONObject();
        try {
            problemJSON.put("id", this.id);
            problemJSON.put("name", this.name);
        } catch (Exception e) {
            Log.e("GETJSON PROBLEMS", "Ошибка сериализации объекта problems в JSON");
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
