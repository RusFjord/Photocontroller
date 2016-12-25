package com.gema.photocontroller.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.models.Problems;

import java.util.ArrayList;
import java.util.List;

public final class PhotoControllerContract {

    public PhotoControllerContract() {}

    static final class JournalEntry implements BaseColumns {

        public final static String TABLE_NAME = "journal";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_IS_SEND = "is_send";
        public final static String COLUMN_TYPE = "type";
        public final static String COLUMN_STATION = "station";
        //public final static String COLUMN_WAGON = "wagon";
        public final static String COLUMN_PROBLEM = "problem";
        public final static String COLUMN_COMMENT = "comment";
    }

    public static final class ProblemsEntry implements BaseColumns {
        public final static String TABLE_NAME = "problems";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_CODE = "code";
        public final static String COLUMN_NAME = "name";

        public static Problems getOneEntry(int id) {
            SQLiteDatabase db = Photocontroler.getDb();
            String[] projection = {
                    _ID,
                    COLUMN_CODE,
                    COLUMN_NAME };
            String selection = _ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            Problems problem = null;
            try (Cursor cursor = db.query(
                    TABLE_NAME,   // таблица
                    projection,            // столбцы
                    selection,                  // столбцы для условия WHERE
                    selectionArgs,                  // значения для условия WHERE
                    null,                  // Don't group the rows
                    null,                  // Don't filter by row groups
                    null)) {
                int idColumnIndex = cursor.getColumnIndex(_ID);
                int nameColumnIndex = cursor.getColumnIndex(COLUMN_NAME);
                int codeColumnIndex = cursor.getColumnIndex(COLUMN_CODE);
                if (cursor.moveToNext()) {
                    int currentID = cursor.getInt(idColumnIndex);
                    String currentName = cursor.getString(nameColumnIndex);
                    String currentCode = cursor.getString(codeColumnIndex);
                    problem = new Problems(currentID, currentCode, currentName);
                }
            }
            return problem;
        }

        public static Problems getOneEntry(String code) {
            SQLiteDatabase db = Photocontroler.getDb();
            String[] projection = {
                    _ID,
                    COLUMN_CODE,
                    COLUMN_NAME };
            String selection = COLUMN_CODE + " = ?";
            String[] selectionArgs = {code};
            Problems problem = null;
            try (Cursor cursor = db.query(
                    TABLE_NAME,   // таблица
                    projection,            // столбцы
                    selection,                  // столбцы для условия WHERE
                    selectionArgs,                  // значения для условия WHERE
                    null,                  // Don't group the rows
                    null,                  // Don't filter by row groups
                    null)) {
                int idColumnIndex = cursor.getColumnIndex(_ID);
                int nameColumnIndex = cursor.getColumnIndex(COLUMN_NAME);
                int codeColumnIndex = cursor.getColumnIndex(COLUMN_CODE);
                if (cursor.moveToNext()) {
                    int currentID = cursor.getInt(idColumnIndex);
                    String currentName = cursor.getString(nameColumnIndex);
                    String currentCode = cursor.getString(codeColumnIndex);
                    problem = new Problems(currentID, currentCode, currentName);
                }
            }
            return problem;
        }

        public static ArrayList<Problems> getAllEntries() {
            SQLiteDatabase db = Photocontroler.getDb();
            ArrayList<Problems> list = new ArrayList<>();
            try (Cursor cursor = db.query(PhotoControllerContract.ProblemsEntry.TABLE_NAME, null, null, null, null, null, null)) {
                int idColumnIndex = cursor.getColumnIndex(PhotoControllerContract.ProblemsEntry._ID);
                int nameColumnIndex = cursor.getColumnIndex(PhotoControllerContract.ProblemsEntry.COLUMN_NAME);
                int codeColumnIndex = cursor.getColumnIndex(PhotoControllerContract.ProblemsEntry.COLUMN_CODE);

                while (cursor.moveToNext()) {
                    // Используем индекс для получения строки или числа
                    int currentID = cursor.getInt(idColumnIndex);
                    String currentName = cursor.getString(nameColumnIndex);
                    String currentCode = cursor.getString(codeColumnIndex);
                    list.add(new Problems(currentID, currentCode, currentName));
                }
            }
            return list;
        }
    }

    public static final class StationsEntry implements BaseColumns {
        public final static String TABLE_NAME = "stations";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_CODE = "code";
        public final static String COLUMN_NAME = "name";
    }

    public static final class FilesMd5Entry implements BaseColumns {
        public final static String TABLE_NAME = "filesmd5";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_FILENAME = "filename";
        public final static String COLUMN_MD5 = "md5";
    }
}
