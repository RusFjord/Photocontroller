package com.gema.photocontroller.commons;

import android.content.Context;
import android.util.Log;

import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.files.WorkFiles;
import com.gema.photocontroller.models.Problems;

import org.json.JSONArray;
import org.json.JSONObject;

public class PreferenceData extends WorkFiles {

    private String version;

    public PreferenceData(String filename, Context context) {
        super(filename);
        readData(context);
    }

    private void readData(Context context) {
        String jsonString = super.ReadFile(context);
        try {
            JSONObject dataJson = new JSONObject(jsonString);
            JSONObject preferenceJson = dataJson.getJSONObject("preference");
            this.version = preferenceJson.getString("version");
        } catch (Exception e) {
            Log.e("PREFERENCE_DATA", e.getMessage());
        }
//            JSONArray problems = dataJson.getJSONArray("problems");
//            for (int i = 0; i < problems.length(); i++) {
//                JSONObject problem = problems.getJSONObject(i);
//                Problems currentProblem = new Problems(problem);
//                //list.add(currentProblem);
//                db.insert(PhotoControllerContract.ProblemsEntry.TABLE_NAME, null, currentProblem.getContentValues());
//            }

    }

    public String getVersion() {
        return this.version;
    }

}
