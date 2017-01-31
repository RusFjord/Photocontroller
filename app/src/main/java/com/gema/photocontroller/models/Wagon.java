package com.gema.photocontroller.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gema.photocontroller.db.PhotoControllerContract;

import org.json.JSONObject;

public class Wagon {

    private long id;
    private String number;
    private String code;
    private String name;
    private WagonType type;

    public Wagon(String number, String code, String name, WagonType type) {
        this.number = number;
        this.type = type;
        this.code = code;
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public WagonType getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public Wagon(JSONObject jsonObject) throws Exception {
        this.number = jsonObject.getString("number");
        this.code = jsonObject.getString("id");
        this.name = jsonObject.getString("name");
        String typeCode = jsonObject.getString("type");
        this.type = PhotoControllerContract.WagonTypeEntry.getOneEntryCode(typeCode);
    }

    public Wagon(Cursor cursor) {
        int idColumnIndex = cursor.getColumnIndex(PhotoControllerContract.WagonEntry._ID);
        int numberColumnIndex = cursor.getColumnIndex(PhotoControllerContract.WagonEntry.COLUMN_NUMBER);
        int codeColumnIndex = cursor.getColumnIndex(PhotoControllerContract.WagonEntry.COLUMN_CODE);
        int nameColumnIndex = cursor.getColumnIndex(PhotoControllerContract.WagonEntry.COLUMN_NAME);
        int wagonTypeColumnIndex = cursor.getColumnIndex(PhotoControllerContract.WagonEntry.COLUMN_WAGON_TYPE);

        this.id = cursor.getLong(idColumnIndex);
        this.number = cursor.getString(numberColumnIndex);
        this.code = cursor.getString(codeColumnIndex);
        this.name = cursor.getString(nameColumnIndex);
        long idWagonType = cursor.getLong(wagonTypeColumnIndex);
        this.type = PhotoControllerContract.WagonTypeEntry.getOneEntry(idWagonType);
    }

    private ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        if (this.id != 0 ) {
            contentValues.put(PhotoControllerContract.WagonTypeEntry._ID, this.id);
        }
        contentValues.put(PhotoControllerContract.WagonEntry.COLUMN_CODE, this.code);
        contentValues.put(PhotoControllerContract.WagonEntry.COLUMN_NAME, this.name);
        contentValues.put(PhotoControllerContract.WagonEntry.COLUMN_NUMBER, this.number);
        if (this.type != null) {
            contentValues.put(PhotoControllerContract.WagonEntry.COLUMN_WAGON_TYPE, this.type.getId());
        }
        return contentValues;
    }

    public void putDb(SQLiteDatabase db) {
        ContentValues contentValues = getContentValues();
        db.insert(PhotoControllerContract.WagonEntry.TABLE_NAME, null, contentValues);
    }
}
