package com.gema.photocontroller.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.interfaces.PlacementAdv;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class PlacementPlace implements PlacementAdv {

    private long id = 0;
    private Date startPlacement;
    private Date stopPlacement;
    private PlaceForAds placeForAds;
    private String aid;
    private String brandName;
    private String layout;

    public PlacementPlace(JSONObject jsonObject) {
        try {
            this.aid = jsonObject.getString("id");
            if (jsonObject.has("placeforads")) {
                JSONObject placeForAdsJSON = jsonObject.getJSONObject("placeforads");
                String code = placeForAdsJSON.getString("id");
                this.placeForAds = PhotoControllerContract.PlaceForAdsEntry.getOneEntry(code);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
            if (jsonObject.has("startPlacement")) {
                String startDate = jsonObject.getString("startPlacement");
                this.startPlacement = dateFormat.parse(startDate);
            }
            if (jsonObject.has("stopPlacement")) {
                String stopDate = jsonObject.getString("stopPlacement");
                this.stopPlacement = dateFormat.parse(stopDate);
            }
            this.brandName = jsonObject.getString("brandname");
            this.layout = jsonObject.getString("layout");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public PlacementPlace(Cursor cursor) {
        int idColumnIndex = cursor.getColumnIndex(PhotoControllerContract.PlacementEntry._ID);
        int startPlacementColumnIndex = cursor.getColumnIndex(PhotoControllerContract.PlacementEntry.COLUMN_START_PLACEMENT);
        int stopPlacementColumnIndex = cursor.getColumnIndex(PhotoControllerContract.PlacementEntry.COLUMN_STOP_PLACEMENT);
        int aidColumnIndex = cursor.getColumnIndex(PhotoControllerContract.PlacementEntry.COLUMN_AID);
        int brandNameColumnIndex = cursor.getColumnIndex(PhotoControllerContract.PlacementEntry.COLUMN_BRANDNAME);
        int placeforadsColumnIndex = cursor.getColumnIndex(PhotoControllerContract.PlacementEntry.COLUMN_PLACEFORADS);
        int layoutColumnIndex = cursor.getColumnIndex(PhotoControllerContract.PlacementEntry.COLUMN_LAYOUT);

        this.id = cursor.getInt(idColumnIndex);
        String startDate = cursor.getString(startPlacementColumnIndex);
        String stopDate = cursor.getString(stopPlacementColumnIndex);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        try {
            this.startPlacement = dateFormat.parse(startDate);
            this.stopPlacement = dateFormat.parse(stopDate);
        } catch (Exception e) {
            Log.e("PLACEMENT DATE FORMAT", "Ошибка форматирования даты");
        }

        this.aid = cursor.getString(aidColumnIndex);
        this.brandName = cursor.getString(brandNameColumnIndex);
        long placeforadsId = cursor.getLong(placeforadsColumnIndex);
        this.placeForAds = PhotoControllerContract.PlaceForAdsEntry.getOneEntry(placeforadsId);
        this.layout = cursor.getString(layoutColumnIndex);
    }

    private String getStringDate(Date date) {
        String result = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        try {
            result = dateFormat.format(date);
        } catch (Exception e) {
            Log.e("PLACEMENT DATE FORMAT", "Ошибка форматирования даты");
        }
        return result;
    }

    public PlaceForAds getPlaceForAds() {
        return this.placeForAds;
    }

    public String getAid() {
        return this.aid;
    }

    @Override
    public long getId() {
        return this.id;
    }

    public Date getStartPlacement() {
        return this.startPlacement;
    }

    public Date getStopPlacement() {
        return this.stopPlacement;
    }

    public String getStartStopPlacement() {
        String result = "";
        if (this.startPlacement != null && this.stopPlacement != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getStringDate(this.startPlacement));
            stringBuilder.append(" - ");
            stringBuilder.append(getStringDate(this.stopPlacement));
            result = stringBuilder.toString();
        }
        return result;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public String getLayout() {
        return this.layout;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject placementPlaceJson = new JSONObject();
        try {
            placementPlaceJson.put("id", this.id);
            placementPlaceJson.put("aid", this.aid);
            placementPlaceJson.put("placeForAds", this.placeForAds.getCode());
        } catch (Exception e) {
            Log.e("PLACEMENT PLACE", "Ошибка формирования json");
        }
        return placementPlaceJson;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PhotoControllerContract.PlacementEntry._ID, this.id == 0 ? null : this.id);
        contentValues.put(PhotoControllerContract.PlacementEntry.COLUMN_AID, this.aid);
        contentValues.put(PhotoControllerContract.PlacementEntry.COLUMN_BRANDNAME, this.brandName);
        contentValues.put(PhotoControllerContract.PlacementEntry.COLUMN_LAYOUT, this.layout);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        String startDate;
        String stopDate;
        try {
            startDate = dateFormat.format(this.startPlacement);
            contentValues.put(PhotoControllerContract.PlacementEntry.COLUMN_START_PLACEMENT, startDate);
            stopDate = dateFormat.format(this.stopPlacement);
            contentValues.put(PhotoControllerContract.PlacementEntry.COLUMN_STOP_PLACEMENT, stopDate);
        } catch (Exception e) {
            Log.e("PLACEMENT DATE FORMAT", "Ошибка форматирования даты");
        }
        if (this.placeForAds != null) {
            contentValues.put(PhotoControllerContract.PlacementEntry.COLUMN_PLACEFORADS, this.placeForAds.getId());
        }
        return contentValues;
    }
}
