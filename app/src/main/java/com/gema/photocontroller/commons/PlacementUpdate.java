package com.gema.photocontroller.commons;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.db.UpdateDbTable;
import com.gema.photocontroller.models.PlacementPlace;

import org.json.JSONArray;
import org.json.JSONObject;

public class PlacementUpdate extends UpdateDbTable {

    public PlacementUpdate(PreferenceData preferenceData) {
        super(preferenceData);
    }

    @Override
    protected void updateTable(SQLiteDatabase db, String fileList) {
        dropDb(db, PhotoControllerContract.PlacementEntry.TABLE_NAME);
        try {
            JSONObject dataJson = new JSONObject(fileList);
            JSONArray placements = dataJson.getJSONArray("placements");
            for (int i = 0; i < placements.length(); i++) {
                JSONObject placement = placements.getJSONObject(i);
                PlacementPlace currentPlace = new PlacementPlace(placement);
                db.replace(PhotoControllerContract.PlacementEntry.TABLE_NAME, null, currentPlace.getContentValues());
            }
        } catch (Exception e) {
            Log.e("PLACEFORADS LIST", e.getMessage());
        }
    }
}
