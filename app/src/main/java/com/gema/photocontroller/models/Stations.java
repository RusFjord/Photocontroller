package com.gema.photocontroller.models;

import android.content.ContentValues;
import android.util.Log;

import com.gema.photocontroller.db.PhotoControllerContract;

import org.json.JSONObject;

public class Stations {

    private int id;
    private String code;
    private String name;

    public Stations(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Stations(JSONObject jsonObject) throws Exception {
        this.code = jsonObject.getString("id");
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

    public int getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public ContentValues getContentValues() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(PhotoControllerContract.StationsEntry.COLUMN_CODE, this.code);
        contentValues.put(PhotoControllerContract.StationsEntry.COLUMN_NAME, this.name);
        return contentValues;
    }
}
