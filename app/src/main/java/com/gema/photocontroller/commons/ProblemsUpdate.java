package com.gema.photocontroller.commons;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.db.UpdateDbTable;
import com.gema.photocontroller.models.Problems;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProblemsUpdate extends UpdateDbTable{

    public ProblemsUpdate(PreferenceData preferenceData) {
        super(preferenceData);
    }

    @Override
    protected void updateTable(SQLiteDatabase db, String fileList) {
        dropDb(db, PhotoControllerContract.ProblemsEntry.TABLE_NAME);
        try {
            JSONObject dataJson = new JSONObject(fileList);
            JSONArray problems = dataJson.getJSONArray("problems");
            for (int i = 0; i < problems.length(); i++) {
                JSONObject problem = problems.getJSONObject(i);
                Problems currentProblem = new Problems(problem);
                db.insert(PhotoControllerContract.ProblemsEntry.TABLE_NAME, null, currentProblem.getContentValues());
            }
        } catch (Exception e) {
            Log.e("PROBLEMS LIST", e.getMessage());
        }
    }
}
