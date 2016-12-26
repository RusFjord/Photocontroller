package com.gema.photocontroller.commons;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.files.WorkFiles;
import com.gema.photocontroller.models.JournalRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class JournalList extends WorkFiles {

    private ArrayList<JournalRecord> list;

    public JournalList(String filename, Context context) {
        super(filename);
        list = new ArrayList<>();
        prepareList(context);
    }

    private void prepareList(Context context) {

        //String fileList = super.ReadFile(context);
//        if (fileList != null) {
//            try {
//                JSONObject dataJson = new JSONObject(fileList);
//                //Log.d("JSON", dataJson.toString());
//                JSONArray records = dataJson.getJSONArray("journal");
//                for (int i = 0; i < records.length(); i++) {
//                    JSONObject record = records.getJSONObject(i);
//                    list.add(new JournalRecord(record));
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

        this.list = PhotoControllerContract.JournalEntry.getAllEntries();
    }

   public void writeJSONArrayIntoFile(Context context) {

        JSONObject journalJSON = new JSONObject();
        try {
            journalJSON.put("journal", getJSONArray());
        } catch (Exception e) {
            e.getStackTrace();
        }
        super.WriteFile(context, "journal.json", journalJSON.toString());

    }

    private JSONArray getJSONArray() {

        JSONArray jsonArray = new JSONArray();
        for (JournalRecord record : this.list) {
            jsonArray.put(record.getJSON());
        }
        return jsonArray;

    }

    public ArrayList<JournalRecord> getList() {
        return list;
    }

    public int getSize() {
        return this.list.size();
    }

    public void add(JournalRecord journalRecord, Context context) {
        this.list.add(journalRecord);
        this.writeJSONArrayIntoFile(context);
    }

    public void sort() {
        Collections.sort(list);
    }

}
