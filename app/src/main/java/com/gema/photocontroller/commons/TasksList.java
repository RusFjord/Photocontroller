package com.gema.photocontroller.commons;

import android.content.Context;

import com.gema.photocontroller.files.WorkFiles;
import com.gema.photocontroller.models.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class TasksList extends WorkFiles {

    private ArrayList<Task> list;

    public TasksList(String filename, Context context) {
        super(filename);
        list = new ArrayList<>();
        prepareList(context);
    }

    private void prepareList(Context context) {
        String fileList = super.ReadFile(context);
        try {
            JSONObject dataJson = new JSONObject(fileList);
            JSONArray tasks = dataJson.getJSONArray("tasks");
            for (int i = 0; i < tasks.length(); i++) {
                JSONObject task = tasks.getJSONObject(i);
                list.add(new Task(task));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Task> getList() {
        return list;
    }

    public void sort() {
        Collections.sort(list);
    }
}
