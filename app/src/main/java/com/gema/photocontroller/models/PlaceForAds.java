package com.gema.photocontroller.models;

import android.content.ContentValues;
import android.util.Log;

import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.interfaces.PlacementAdv;

import org.json.JSONObject;

public class PlaceForAds implements PlacementAdv {

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

    @Override
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
            placeForAdsJSON.put("code", this.code);
            placeForAdsJSON.put("name", this.name);
        } catch (Exception e) {
            Log.e("PLACEFORADS", "Ошибка формирования json");
        }
        return placeForAdsJSON;
    }

    public ContentValues getContentValues() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(PhotoControllerContract.PlaceForAdsEntry.COLUMN_CODE, this.code);
        contentValues.put(PhotoControllerContract.PlaceForAdsEntry.COLUMN_NAME, this.name);
        return contentValues;
    }

    @Override
    public String getRepresentation() {
        StringBuilder representation = new StringBuilder(this.code);
        representation.append(" ");
        representation.append(this.name);
        return representation.toString();
    }
}
