package com.gema.photocontroller.commons;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.files.WorkFiles;
import com.gema.photocontroller.models.PlacementPlace;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlacementList extends WorkFiles {

    private final String FILENAME = "placement.json";
    private ArrayList<PlacementPlace> list;

    public PlacementList(Context context, String filename) {
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
//        try {
//            JSONObject dataJson = new JSONObject(fileList);
//            JSONArray placements = dataJson.getJSONArray("placements");
//            for (int i = 0; i < placements.length(); i++) {
//                JSONObject placement = placements.getJSONObject(i);
//                list.add(new PlacementPlace(placement));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public ArrayList<PlacementPlace> getList() {
        return list;
    }

    private void updateTable(SQLiteDatabase db, String fileList) {

        String dropQuery = "DELETE FROM " + PhotoControllerContract.PlacementEntry.TABLE_NAME + "; VACUUM;";
        db.execSQL(dropQuery);
        try {
            JSONObject dataJson = new JSONObject(fileList);
            JSONArray placements = dataJson.getJSONArray("placements");
            for (int i = 0; i < placements.length(); i++) {
                JSONObject placement = placements.getJSONObject(i);
                PlacementPlace currentPlace = new PlacementPlace(placement);
                db.replace(PhotoControllerContract.PlacementEntry.TABLE_NAME, null, currentPlace.getContentValues());
            }
            String currentMd5 = Photocontroler.getMD5EncryptedString(fileList);
            db.beginTransaction();
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
            } finally {
                db.endTransaction();
            }

        } catch (Exception e) {
            Log.e("PLACEFORADS LIST", e.getMessage());
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

    @Deprecated
    public PlacementPlace getElement(int index) {
        PlacementPlace place = list.get(index);
        return place;
    }
}
