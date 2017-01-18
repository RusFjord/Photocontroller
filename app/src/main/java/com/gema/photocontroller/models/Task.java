package com.gema.photocontroller.models;

import com.gema.photocontroller.interfaces.PlacementAdv;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task implements Comparable {

    private Date date;
    private String type;
    private PlaceForAds placeForAds;
    private String layout;

    public Task(Date date, String type, PlaceForAds placeForAds, String layout) {
        this.date = date;
        this.type = type;
        this.placeForAds = placeForAds;
        this.layout = layout;
    }

    public Task(JSONObject jsonObject) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
            this.date = dateFormat.parse(jsonObject.getString("date"));
            this.type = jsonObject.getString("type");
            if (jsonObject.has("placeforads")) {
                JSONObject placeForAdsJSON = jsonObject.getJSONObject("placeforads");
                this.placeForAds = new PlaceForAds(placeForAdsJSON);
            }
            this.layout = jsonObject.getString("layout");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public Date getDate() {
        return this.date;
    }

    public String getType() {
        return this.type;
    }

    public PlacementAdv getPlaceForAds() {
        return this.placeForAds;
    }

    public String getLayout() {
        return this.layout;
    }

    @Override
    public int compareTo(Object o) {
        Task otherTask = (Task) o;
        return (int) (otherTask.getDate().getTime() - this.date.getTime());
    }
}
