package com.gema.photocontroller.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.gema.photocontroller.db.PhotoControllerContract;

import org.json.JSONObject;

public class WagonType {

    private long id = 0;
    private String code;
    private String name;


    public WagonType(long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public WagonType(Cursor cursor) {
//        int idColumnIndex = cursor.getColumnIndex(_ID);
//                int startPlacementColumnIndex = cursor.getColumnIndex(COLUMN_START_PLACEMENT);
//                int stopPlace
        int idColumnIndex = cursor.getColumnIndex(PhotoControllerContract.WagonTypeEntry._ID);
        int codeColumnIndex = cursor.getColumnIndex(PhotoControllerContract.WagonTypeEntry.COLUMN_CODE);
        int nameColumnIndex = cursor.getColumnIndex(PhotoControllerContract.WagonTypeEntry.COLUMN_NAME);

        this.id = cursor.getLong(idColumnIndex);
        this.code = cursor.getString(codeColumnIndex);
        this.name = cursor.getString(nameColumnIndex);
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public WagonType(JSONObject jsonObject) throws Exception {

        this.code = jsonObject.getString("code");
        this.name = jsonObject.getString("name");
    }

    public ContentValues getContentValues() {

        ContentValues contentValues = new ContentValues();
        if (this.id != 0 ) {
            contentValues.put(PhotoControllerContract.WagonTypeEntry._ID, this.id);
        }
        contentValues.put(PhotoControllerContract.WagonTypeEntry.COLUMN_CODE, this.code);
        contentValues.put(PhotoControllerContract.WagonTypeEntry.COLUMN_NAME, this.name);
        return contentValues;
    }
}
