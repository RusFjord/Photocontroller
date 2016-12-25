package com.gema.photocontroller.models;

import org.json.JSONObject;

public class PlaceForAds {

    private int id;
    private String code;
    private String name;

    public PlaceForAds(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public PlaceForAds(JSONObject jsonObject) throws Exception {
        this.code = jsonObject.getString("id");
        this.name = jsonObject.getString("name");
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
}
