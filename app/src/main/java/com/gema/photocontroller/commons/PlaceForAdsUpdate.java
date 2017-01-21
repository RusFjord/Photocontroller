package com.gema.photocontroller.commons;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.db.UpdateDbTable;
import com.gema.photocontroller.models.PlaceForAds;

import org.json.JSONArray;
import org.json.JSONObject;

public class PlaceForAdsUpdate extends UpdateDbTable {
    @Override
    protected void updateTable(SQLiteDatabase db, String fileList) {
        dropDb(db, PhotoControllerContract.PlaceForAdsEntry.TABLE_NAME);
        try {
            JSONObject dataJson = new JSONObject(fileList);
            JSONArray placeforadses = dataJson.getJSONArray("placeforads");
            for (int i = 0; i < placeforadses.length(); i++) {
                JSONObject placeforads = placeforadses.getJSONObject(i);
                PlaceForAds currentPlaceforads = new PlaceForAds(placeforads);
                db.insert(PhotoControllerContract.PlaceForAdsEntry.TABLE_NAME, null, currentPlaceforads.getContentValues());
            }
        } catch (Exception e) {
            Log.e("PLACEFORADS LIST", e.getMessage());
        }
    }
}
