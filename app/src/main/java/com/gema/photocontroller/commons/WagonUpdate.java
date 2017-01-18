package com.gema.photocontroller.commons;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.db.UpdateDbTable;
import com.gema.photocontroller.models.Wagon;

import org.json.JSONArray;
import org.json.JSONObject;

public class WagonUpdate extends UpdateDbTable {

    @Override
    protected void updateTable(SQLiteDatabase db, String fileList) {
        dropDb(db, PhotoControllerContract.WagonEntry.TABLE_NAME);
        try {
            JSONObject dataJson = new JSONObject(fileList);
            JSONArray jsonArray = dataJson.getJSONArray("wagons");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject wagonJson = jsonArray.getJSONObject(i);
                Wagon currentWagon = new Wagon(wagonJson);
                currentWagon.putDb(db);
            }

        } catch (Exception e) {
            Log.e("WAGON UPDATE", e.getMessage());
        }
    }
}
