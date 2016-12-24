package com.gema.photocontroller.commons;

import android.content.Context;
import android.util.Log;

import com.gema.photocontroller.files.WorkFiles;
import com.gema.photocontroller.models.Stations;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class StationsList extends WorkFiles{

    private ArrayList<Stations> list;

    public StationsList(String filename, Context context) {
        super(filename);
        list = new ArrayList<Stations>();
        prepareList(context);
    }

    private void prepareList(Context context) {
        String fileList = super.ReadFile(context);
        try {
            JSONObject dataJson = new JSONObject(fileList);
            JSONArray problems = dataJson.getJSONArray("stations");
            for (int i = 0; i < problems.length(); i++) {
                JSONObject problem = problems.getJSONObject(i);
                list.add(new Stations(problem));
            }

        } catch (Exception e) {
            Log.e("STATIONS LIST", e.getMessage());
        }
    }

    public ArrayList<Stations> getList() {
        return this.list;
    }

}
