package com.gema.photocontroller.models;

import org.json.JSONObject;

public class PlacementPlace {

    private PlaceForAds placeForAds;
    private String id;
    private String brandName;
    private String layout;

    public PlacementPlace(PlaceForAds placeForAds, String id, String brandName, String layout) {
        this.placeForAds = placeForAds;
        this.id = id;
        this.brandName = brandName;
        this.layout = layout;
    }

    public PlacementPlace(String[] data) {
        this.placeForAds = new PlaceForAds(data[1], data[2]);
        this.id = data[0];
        this.brandName = data[3];
        this.layout = data[4];
    }

    public PlacementPlace(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getString("id");
            if (jsonObject.has("placeforads")) {
                JSONObject placeForAdsJSON = jsonObject.getJSONObject("placeforads");
                this.placeForAds = new PlaceForAds(placeForAdsJSON);
            }
            this.brandName = jsonObject.getString("brandname");
            this.layout = jsonObject.getString("layout");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public PlaceForAds getPlaceForAds() {
        return this.placeForAds;
    }

    public String getId() {
        return this.id;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public String getLayout() {
        return this.layout;
    }

}
