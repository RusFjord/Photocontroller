package com.gema.photocontroller.commons;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.files.WorkFiles;
import com.gema.photocontroller.models.PlaceForAds;


import org.json.JSONArray;
import org.json.JSONObject;

public class PlaceForAdsList extends WorkFiles{

    private final String FILENAME = "placeforads.json";

    //private ArrayList<PlacementAdv> list;

    public PlaceForAdsList(Context context, String filename) {
        super(filename);
        //list = new ArrayList<PlacementAdv>();
        //prepareList(context);
    }

    public void prepareList(Context context) {

        //String fileList = super.ReadFile(context);
        String fileList = super.ReadFile(context);
        SQLiteDatabase db = Photocontroler.getDb();

        if (!md5Equals(db, fileList)) {
            updateTable(db, fileList);
        }
    }

    private void updateTable(SQLiteDatabase db, String fileList) {

        String dropQuery = "DELETE FROM " + PhotoControllerContract.PlaceForAdsEntry.TABLE_NAME + "; VACUUM;";
        db.execSQL(dropQuery);
        try {
            JSONObject dataJson = new JSONObject(fileList);
            JSONArray placeforadses = dataJson.getJSONArray("placeforads");
            //db.beginTransaction();
            for (int i = 0; i < placeforadses.length(); i++) {
                JSONObject placeforads = placeforadses.getJSONObject(i);
                PlaceForAds currentPlaceforads = new PlaceForAds(placeforads);
                db.insert(PhotoControllerContract.PlaceForAdsEntry.TABLE_NAME, null, currentPlaceforads.getContentValues());
            }
            //db.endTransaction();
            String currentMd5 = Photocontroler.getMD5EncryptedString(fileList);
            //db.beginTransaction();
            try (Cursor cursor = db.rawQuery("select md5 from " + PhotoControllerContract.FilesMd5Entry.TABLE_NAME + " where filename = ?", new String[]{FILENAME})) {
                int idColumnIndex = cursor.getColumnIndex(PhotoControllerContract.FilesMd5Entry._ID);
                long rowIndex = 0;
                while (cursor.moveToNext()) {
                    rowIndex = cursor.getInt(idColumnIndex);
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put(PhotoControllerContract.FilesMd5Entry._ID, (rowIndex == 0 ? null : rowIndex));
                contentValues.put(PhotoControllerContract.FilesMd5Entry.COLUMN_FILENAME, FILENAME);
                contentValues.put(PhotoControllerContract.FilesMd5Entry.COLUMN_MD5, currentMd5);
                db.replace(PhotoControllerContract.FilesMd5Entry.TABLE_NAME, null, contentValues);
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

//    public ArrayList<PlacementAdv> getList() {
//        return list;
//    }

}
