package com.gema.photocontroller.commons;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.db.UpdateDbTable;
import com.gema.photocontroller.models.PlaceForAds;

import org.json.JSONArray;
import org.json.JSONObject;

public class PlaceForAdsUpdate extends UpdateDbTable {
    @Override
    protected void updateTable(SQLiteDatabase db, String fileList) {
        dropDb(db, PhotoControllerContract.PlaceForAdsEntry.TABLE_NAME);
        try {
            JSONObject dataJson = new JSONObject(fileList);
            JSONArray placeforadses = dataJson.getJSONArray("placeforads");
            for (int i = 0; i < placeforadses.length(); i++) {
                JSONObject placeforads = placeforadses.getJSONObject(i);
                PlaceForAds currentPlaceforads = new PlaceForAds(placeforads);
                db.insert(PhotoControllerContract.PlaceForAdsEntry.TABLE_NAME, null, currentPlaceforads.getContentValues());
            }
//            String currentMd5 = Photocontroler.getMD5EncryptedString(fileList);
//            try (Cursor cursor = db.rawQuery("select md5 from " + PhotoControllerContract.FilesMd5Entry.TABLE_NAME + " where filename = ?", new String[]{FILENAME})) {
//                int idColumnIndex = cursor.getColumnIndex(PhotoControllerContract.FilesMd5Entry._ID);
//                long rowIndex = 0;
//                while (cursor.moveToNext()) {
//                    rowIndex = cursor.getInt(idColumnIndex);
//                }
//                ContentValues contentValues = new ContentValues();
//                contentValues.put(PhotoControllerContract.FilesMd5Entry._ID, (rowIndex == 0 ? null : rowIndex));
//                contentValues.put(PhotoControllerContract.FilesMd5Entry.COLUMN_FILENAME, FILENAME);
//                contentValues.put(PhotoControllerContract.FilesMd5Entry.COLUMN_MD5, currentMd5);
//                db.replace(PhotoControllerContract.FilesMd5Entry.TABLE_NAME, null, contentValues);
//            }

        } catch (Exception e) {
            Log.e("PLACEFORADS LIST", e.getMessage());
        }
    }
}
