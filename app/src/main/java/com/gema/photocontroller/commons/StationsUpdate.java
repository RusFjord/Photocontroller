package com.gema.photocontroller.commons;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.db.UpdateDbTable;
import com.gema.photocontroller.models.Stations;

import org.json.JSONArray;
import org.json.JSONObject;

public class StationsUpdate extends UpdateDbTable{

    public StationsUpdate(PreferenceData preferenceData) {
        super(preferenceData);
    }

    @Override
    protected void updateTable(SQLiteDatabase db, String fileList) {
        dropDb(db, PhotoControllerContract.StationsEntry.TABLE_NAME);
        try {
            JSONObject dataJson = new JSONObject(fileList);
            JSONArray stations = dataJson.getJSONArray("stations");
            for (int i = 0; i < stations.length(); i++) {
                JSONObject station = stations.getJSONObject(i);
                Stations currentStation = new Stations(station);
                db.insert(PhotoControllerContract.StationsEntry.TABLE_NAME, null, currentStation.getContentValues());
            }
        } catch (Exception e) {
            Log.e("STATIONS LIST", e.getMessage());
        }
    }
}
