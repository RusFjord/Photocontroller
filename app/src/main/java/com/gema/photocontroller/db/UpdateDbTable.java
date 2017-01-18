package com.gema.photocontroller.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.files.WorkFiles;

public abstract class UpdateDbTable extends WorkFiles {

    private String filename = "";

    public UpdateDbTable() {
        super("");
    }

    public void prepareTable(Context context, String filename) {
        super.setFilename(filename);
        String fileList = super.ReadFile(context);
        SQLiteDatabase db = Photocontroler.getDb();

        if (!md5Equals(db, fileList)) {
            updateTable(db, fileList);
            setMd5(db, fileList);
        }
    }

    protected abstract void updateTable(SQLiteDatabase db, String fileList);

    private boolean md5Equals(SQLiteDatabase db, String fileList) {
        boolean result = false;
        String currentMD5 = null;
        try (Cursor cursor = db.rawQuery("select md5 from " + PhotoControllerContract.FilesMd5Entry.TABLE_NAME + " where filename = ?", new String[]{filename})) {
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

    private void setMd5(SQLiteDatabase db, String fileList) {
        String currentMd5 = Photocontroler.getMD5EncryptedString(fileList);
        try (Cursor cursor = db.rawQuery("select md5 from " + PhotoControllerContract.FilesMd5Entry.TABLE_NAME + " where filename = ?", new String[]{filename})) {
            int idColumnIndex = cursor.getColumnIndex(PhotoControllerContract.FilesMd5Entry._ID);
            long rowIndex = 0;
            while (cursor.moveToNext()) {
                rowIndex = cursor.getInt(idColumnIndex);
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put(PhotoControllerContract.FilesMd5Entry._ID, rowIndex);
            contentValues.put(PhotoControllerContract.FilesMd5Entry.COLUMN_FILENAME, filename);
            contentValues.put(PhotoControllerContract.FilesMd5Entry.COLUMN_MD5, currentMd5);
            db.replace(PhotoControllerContract.FilesMd5Entry.TABLE_NAME, null, contentValues);
        }
    }

    protected void dropDb(SQLiteDatabase db, String tableName) {
        String dropQuery = "DELETE FROM " + tableName + "; VACUUM;";
        db.execSQL(dropQuery);
    }
}
