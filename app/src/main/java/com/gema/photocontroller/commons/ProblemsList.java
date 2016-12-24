package com.gema.photocontroller.commons;

import android.content.Context;
import android.util.Log;

import com.gema.photocontroller.files.WorkFiles;
import com.gema.photocontroller.models.Problems;
import com.gema.photocontroller.models.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProblemsList extends WorkFiles {

    private ArrayList<Problems> list;

    public ProblemsList(String filename, Context context) {
        super(filename);
        list = new ArrayList<Problems>();
        prepareList(context);
    }

    private void prepareList(Context context) {
        String fileList = super.ReadFile(context);
        try {
            JSONObject dataJson = new JSONObject(fileList);
            JSONArray problems = dataJson.getJSONArray("problems");
            for (int i = 0; i < problems.length(); i++) {
                JSONObject problem = problems.getJSONObject(i);
                list.add(new Problems(problem));
            }

        } catch (Exception e) {
            Log.e("PROBLEMS LIST", e.getMessage());
        }
    }

    public ArrayList<Problems> getList() {
        return this.list;
    }

}
