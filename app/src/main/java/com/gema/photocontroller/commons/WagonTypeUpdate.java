package com.gema.photocontroller.commons;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.db.UpdateDbTable;
import com.gema.photocontroller.models.WagonType;

import org.json.JSONArray;
import org.json.JSONObject;

public class WagonTypeUpdate extends UpdateDbTable {

    public WagonTypeUpdate(PreferenceData preferenceData) {
        super(preferenceData);
    }

    @Override
    protected void updateTable(SQLiteDatabase db, String fileList) {
        dropDb(db, PhotoControllerContract.WagonTypeEntry.TABLE_NAME);
        try {
            JSONObject dataJson = new JSONObject(fileList);
            JSONArray jsonArray = dataJson.getJSONArray("wagontypes");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject wagonTypeJson = jsonArray.getJSONObject(i);
                WagonType currentWagonType = new WagonType(wagonTypeJson);
                currentWagonType.putDb(db);
            }

        } catch (Exception e) {
            Log.e("WAGON TYPE UPDATE", e.getMessage());
        }
    }
}
