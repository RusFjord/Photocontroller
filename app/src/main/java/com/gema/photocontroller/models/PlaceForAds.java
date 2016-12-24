package com.gema.photocontroller.models;

import org.json.JSONObject;

public class PlaceForAds {

    private String id;
    private String name;

    public PlaceForAds(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public PlaceForAds(JSONObject jsonObject) throws Exception {
        this.id = jsonObject.getString("id");
        this.name = jsonObject.getString("name");
    }

    public String getId() {
        return this.id;
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
