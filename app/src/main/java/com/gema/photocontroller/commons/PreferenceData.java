package com.gema.photocontroller.commons;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.files.WorkFiles;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PreferenceData extends WorkFiles {

    private String version;
    private String password;
    private ArrayList<String> listForDownload;
    private HashMap<String, String> remoteMd5;

    public PreferenceData(String filename, Context context) {
        super(filename);
        this.listForDownload = new ArrayList<>();
        this.remoteMd5 = new HashMap<>();
        readData(context);
    }

    private void readData(Context context) {
        String jsonString = super.ReadFile(context);
        try {
            JSONObject dataJson = new JSONObject(jsonString);
            JSONObject preferenceJson = dataJson.getJSONObject("preference");
            this.version = preferenceJson.getString("version");
            this.password = preferenceJson.getString("password");
            JSONArray list = preferenceJson.getJSONArray("downloads");
            prepareListFiles(list);
        } catch (Exception e) {
            Log.e("PREFERENCE DATA", e.getMessage());
        }
    }

    public String getVersion() {
        return this.version;
    }

    private void prepareListFiles(JSONArray list) throws Exception {
        listForDownload.add("tasks.zip");
        SQLiteDatabase db = Photocontroler.getDb();
        int length = list.length();
        for (int i = 0; i < length; i++) {
            JSONObject object = list.getJSONObject(i);
            String filename = object.getString("filename");
            String newMd5 = object.getString("md5");
            this.remoteMd5.put(filename, newMd5);
            if (!md5Equals(db, filename + ".json", newMd5)) {
                listForDownload.add(filename + ".zip");
            }
        }
    }

    private boolean md5Equals(SQLiteDatabase db, String filename, String newMd5) {

        boolean result = false;
        String currentMD5 = null;
        try (Cursor cursor = db.rawQuery("select md5 from " + PhotoControllerContract.FilesMd5Entry.TABLE_NAME + " where filename = ?", new String[]{filename})) {
            int md5ColumnIndex = cursor.getColumnIndex(PhotoControllerContract.FilesMd5Entry.COLUMN_MD5);

            while (cursor.moveToNext()) {
                currentMD5 = cursor.getString(md5ColumnIndex);
            }
        }
        if (currentMD5 != null) {
            if (newMd5.equals(currentMD5)) {
                result = true;
            }
        }
        return result;
    }

    public ArrayList<String> getFilesForDownload() {
        return this.listForDownload;
    }

    public void updatePreference(Context context) {
        AppPreference preference = new AppPreference(context);
        preference.setPasswordApp(this.password);
    }

    public boolean tryRemoteMd5(String filename) {
        boolean result = false;
        String currentMD5 = null;
        SQLiteDatabase db = Photocontroler.getDb();
        try (Cursor cursor = db.rawQuery("select md5 from " + PhotoControllerContract.FilesMd5Entry.TABLE_NAME + " where filename = ?", new String[]{filename})) {
            int md5ColumnIndex = cursor.getColumnIndex(PhotoControllerContract.FilesMd5Entry.COLUMN_MD5);

            while (cursor.moveToNext()) {
                currentMD5 = cursor.getString(md5ColumnIndex);
            }
        }
        if (currentMD5 == null) {
            result = true;
        } else {
            String remoteMD5 = this.remoteMd5.get(filename);
            if (remoteMD5 != null) {
                if (!currentMD5.equals(remoteMD5)) {
                    result = true;
                }
            }
        }
        return result;
    }

    public String getCurrentMd5(String filename) {
        return this.remoteMd5.get(filename);
    }
}
