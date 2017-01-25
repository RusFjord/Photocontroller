package com.gema.photocontroller.commons;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.files.WorkFiles;
import com.gema.photocontroller.models.Stations;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class StationsList extends WorkFiles{

    private final String FILENAME = "stations.json";

    private ArrayList<Stations> list;

    public StationsList(String filename, Context context) {
        super(filename);
        list = new ArrayList<>();
        prepareList(context);
    }

    private void prepareList(Context context) {

        String fileList = super.ReadFile(context);
        SQLiteDatabase db = Photocontroler.getDb();

        if (!md5Equals(db, fileList)) {
            updateTable(db, fileList);
        }
        //this.list = PhotoControllerContract.StationsEntry.getAllEntries();
    }

    private void updateTable(SQLiteDatabase db, String fileList) {

        String dropQuery = "DELETE FROM " + PhotoControllerContract.StationsEntry.TABLE_NAME + "; VACUUM;";
        db.execSQL(dropQuery);
        try {
            JSONObject dataJson = new JSONObject(fileList);
            JSONArray stations = dataJson.getJSONArray("stations");
            for (int i = 0; i < stations.length(); i++) {
                JSONObject station = stations.getJSONObject(i);
                Stations currentStation = new Stations(station);
                db.insert(PhotoControllerContract.StationsEntry.TABLE_NAME, null, currentStation.getContentValues());
            }
            String currentMd5 = Photocontroler.getMD5EncryptedString(fileList);
            try (Cursor cursor = db.rawQuery("select md5 from " + PhotoControllerContract.FilesMd5Entry.TABLE_NAME + " where filename = ?", new String[]{FILENAME})) {
                int idColumnIndex = cursor.getColumnIndex(PhotoControllerContract.FilesMd5Entry._ID);
                long rowIndex = 0;
                while (cursor.moveToNext()) {
                    rowIndex = cursor.getInt(idColumnIndex);
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put(PhotoControllerContract.FilesMd5Entry._ID, rowIndex);
                contentValues.put(PhotoControllerContract.FilesMd5Entry.COLUMN_FILENAME, FILENAME);
                contentValues.put(PhotoControllerContract.FilesMd5Entry.COLUMN_MD5, currentMd5);
                db.replace(PhotoControllerContract.FilesMd5Entry.TABLE_NAME, null, contentValues);
            }

        } catch (Exception e) {
            Log.e("STATIONS LIST", e.getMessage());
        }

    }

    private boolean md5Equals(SQLiteDatabase db, String fileList) {

        boolean result = false;
        String currentMD5 = null;
        try (Cursor cursor = db.rawQuery("select md5 from " + PhotoControllerContract.FilesMd5Entry.TABLE_NAME + " where filename = ?", new String[]{FILENAME})) {
            int md5ColumnIndex = cursor.getColumnIndex(PhotoControllerContract.FilesMd5Entry.COLUMN_MD5);

            while (cursor.moveToNext()) {
                currentMD5 = cursor.getString(md5ColumnIndex);
            }
        }
        if (currentMD5 != null) {
            String newMd5 = Photocontroler.getMD5EncryptedString(fileList);
            if (newMd5.equals(currentMD5)) {
                result = true;
            }
        }
        return result;
    }

    public ArrayList<Stations> getList() {
        return this.list;
    }

}
