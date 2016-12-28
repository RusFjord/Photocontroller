package com.gema.photocontroller.models;

import android.content.ContentValues;

import com.gema.photocontroller.db.PhotoControllerContract;

import org.json.JSONObject;

public class PlaceForAds {

    private long id;
    private String code;
    private String name;

    public PlaceForAds(long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public PlaceForAds(JSONObject jsonObject) throws Exception {
        this.code = jsonObject.getString("id");
        this.name = jsonObject.getString("name");
    }

    public long getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public JSONObject getJSON() {
        JSONObject placeForAdsJSON = new JSONObject();
        try {
            placeForAdsJSON.put("id", this.id);
            placeForAdsJSON.put("name", this.name);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return placeForAdsJSON;
    }

    public ContentValues getContentValues() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(PhotoControllerContract.PlaceForAdsEntry.COLUMN_CODE, this.code);
        contentValues.put(PhotoControllerContract.PlaceForAdsEntry.COLUMN_NAME, this.name);
        return contentValues;
    }
}
