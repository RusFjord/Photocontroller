package com.gema.photocontroller.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.models.JournalRecord;
import com.gema.photocontroller.models.PlaceForAds;
import com.gema.photocontroller.models.Problems;
import com.gema.photocontroller.models.Stations;

import java.util.ArrayList;
import java.util.List;

public final class PhotoControllerContract {

    public PhotoControllerContract() {}

    public static final class JournalEntry implements BaseColumns {

        public final static String TABLE_NAME = "journal";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_IS_SEND = "is_send";
        public final static String COLUMN_TYPE = "type";
        public final static String COLUMN_STATION = "station";
        public final static String COLUMN_PLACEFORADS = "placeforads";
        //public final static String COLUMN_WAGON = "wagon";
        public final static String COLUMN_PROBLEM = "problem";
        public final static String COLUMN_COMMENT = "comment";

        public static JournalRecord getOneEntry(int id) {
            SQLiteDatabase db = Photocontroler.getDb();
            String[] projection = {
                    _ID,
                    COLUMN_DATE,
                    COLUMN_IS_SEND,
                    COLUMN_TYPE,
                    COLUMN_COMMENT,
                    COLUMN_PROBLEM,
                    COLUMN_PLACEFORADS,
                    COLUMN_STATION};
            String selection = _ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            JournalRecord journalRecord = null;
            try (Cursor cursor = db.query(
                    TABLE_NAME,   // таблица
                    projection,            // столбцы
                    selection,                  // столбцы для условия WHERE
                    selectionArgs,                  // значения для условия WHERE
                    null,                  // Don't group the rows
                    null,                  // Don't filter by row groups
                    null)) {

                if (cursor.moveToNext()) {

                    journalRecord = new JournalRecord(cursor);
                }
            }
            return journalRecord;
        }

        public static ArrayList<JournalRecord> getAllEntries() {
            SQLiteDatabase db = Photocontroler.getDb();
            ArrayList<JournalRecord> list = new ArrayList<>();
            try (Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null)) {
//                int idColumnIndex = cursor.getColumnIndex(_ID);
//                int nameColumnIndex = cursor.getColumnIndex(COLUMN_NAME);
//                int codeColumnIndex = cursor.getColumnIndex(COLUMN_CODE);

                while (cursor.moveToNext()) {
                    // Используем индекс для получения строки или числа
//                    int currentID = cursor.getInt(idColumnIndex);
//                    String currentName = cursor.getString(nameColumnIndex);
//                    String currentCode = cursor.getString(codeColumnIndex);
                    list.add(new JournalRecord(cursor));
                }
            }
            return list;
        }
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
            try (Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null)) {
                int idColumnIndex = cursor.getColumnIndex(_ID);
                int nameColumnIndex = cursor.getColumnIndex(COLUMN_NAME);
                int codeColumnIndex = cursor.getColumnIndex(COLUMN_CODE);

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

        public static Stations getOneEntry(int id) {
            SQLiteDatabase db = Photocontroler.getDb();
            String[] projection = {
                    _ID,
                    COLUMN_CODE,
                    COLUMN_NAME };
            String selection = _ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            Stations station = null;
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
                    station = new Stations(currentID, currentCode, currentName);
                }
            }
            return station;
        }

        public static Stations getOneEntry(String code) {
            SQLiteDatabase db = Photocontroler.getDb();
            String[] projection = {
                    _ID,
                    COLUMN_CODE,
                    COLUMN_NAME };
            String selection = COLUMN_CODE + " = ?";
            String[] selectionArgs = {code};
            Stations station = null;
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
                    station = new Stations(currentID, currentCode, currentName);
                }
            }
            return station;
        }

        public static ArrayList<Stations> getAllEntries() {
            SQLiteDatabase db = Photocontroler.getDb();
            ArrayList<Stations> list = new ArrayList<>();
            try (Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null)) {
                int idColumnIndex = cursor.getColumnIndex(_ID);
                int nameColumnIndex = cursor.getColumnIndex(COLUMN_NAME);
                int codeColumnIndex = cursor.getColumnIndex(COLUMN_CODE);

                while (cursor.moveToNext()) {
                    // Используем индекс для получения строки или числа
                    int currentID = cursor.getInt(idColumnIndex);
                    String currentName = cursor.getString(nameColumnIndex);
                    String currentCode = cursor.getString(codeColumnIndex);
                    list.add(new Stations(currentID, currentCode, currentName));
                }
            }
            return list;
        }
    }

    public static final class PlaceForAdsEntry implements BaseColumns {

        public final static String TABLE_NAME = "placeforads";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_CODE = "code";
        public final static String COLUMN_NAME = "name";

        public static PlaceForAds getOneEntry(int id) {
            SQLiteDatabase db = Photocontroler.getDb();
            String[] projection = {
                    _ID,
                    COLUMN_CODE,
                    COLUMN_NAME };
            String selection = _ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            PlaceForAds placeForAds = null;
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
                    placeForAds = new PlaceForAds(currentID, currentCode, currentName);
                }
            }
            return placeForAds;
        }

        public static PlaceForAds getOneEntry(String code) {
            SQLiteDatabase db = Photocontroler.getDb();
            String[] projection = {
                    _ID,
                    COLUMN_CODE,
                    COLUMN_NAME };
            String selection = COLUMN_CODE + " = ?";
            String[] selectionArgs = {code};
            PlaceForAds placeForAds = null;
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
                    placeForAds = new PlaceForAds(currentID, currentCode, currentName);
                }
            }
            return placeForAds;
        }

        public static ArrayList<PlaceForAds> getAllEntries() {
            SQLiteDatabase db = Photocontroler.getDb();
            ArrayList<PlaceForAds> list = new ArrayList<>();
            try (Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null)) {
                int idColumnIndex = cursor.getColumnIndex(_ID);
                int nameColumnIndex = cursor.getColumnIndex(COLUMN_NAME);
                int codeColumnIndex = cursor.getColumnIndex(COLUMN_CODE);

                while (cursor.moveToNext()) {
                    // Используем индекс для получения строки или числа
                    int currentID = cursor.getInt(idColumnIndex);
                    String currentName = cursor.getString(nameColumnIndex);
                    String currentCode = cursor.getString(codeColumnIndex);
                    list.add(new PlaceForAds(currentID, currentCode, currentName));
                }
            }
            return list;
        }

        public static Cursor getAllEntriesCursor() {

            SQLiteDatabase db = Photocontroler.getDb();
            Cursor cursor = null;
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            return cursor;
        }

        public static Cursor getFilterCodeEntries(String filter) {

            SQLiteDatabase db = Photocontroler.getDb();
            String[] projection = {
                    _ID,
                    COLUMN_CODE,
                    COLUMN_NAME };
            String selection = COLUMN_CODE + " like ?";
            String[] selectionArgs = {"%" + filter + "%"};
            Cursor cursor = db.query(
                    TABLE_NAME,   // таблица
                    projection,            // столбцы
                    selection,                  // столбцы для условия WHERE
                    selectionArgs,                  // значения для условия WHERE
                    null,                  // Don't group the rows
                    null,                  // Don't filter by row groups
                    null);
            return cursor;
        }
    }

    public static final class FilesMd5Entry implements BaseColumns {
        public final static String TABLE_NAME = "filesmd5";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_FILENAME = "filename";
        public final static String COLUMN_MD5 = "md5";
    }
}
