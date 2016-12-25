package com.gema.photocontroller.commons;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.files.WorkFiles;
import com.gema.photocontroller.models.Problems;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProblemsList extends WorkFiles {

    private final String FILENAME = "problems.json";
    private ArrayList<Problems> list;

    public ProblemsList(String filename, Context context) {
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
        this.list = PhotoControllerContract.ProblemsEntry.getAllEntries();
    }

    private void updateTable(SQLiteDatabase db, String fileList) {

        String dropQuery = "DELETE FROM " + PhotoControllerContract.FilesMd5Entry.TABLE_NAME + "; VACUUM;";
        db.execSQL(dropQuery);
        try {
            JSONObject dataJson = new JSONObject(fileList);
            JSONArray problems = dataJson.getJSONArray("problems");
            for (int i = 0; i < problems.length(); i++) {
                JSONObject problem = problems.getJSONObject(i);
                Problems currentProblem = new Problems(problem);
                //list.add(currentProblem);
                db.insert(PhotoControllerContract.ProblemsEntry.TABLE_NAME, null, currentProblem.getContentValues());
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
            Log.e("PROBLEMS LIST", e.getMessage());
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

    public ArrayList<Problems> getList() {
        return this.list;
    }

}
