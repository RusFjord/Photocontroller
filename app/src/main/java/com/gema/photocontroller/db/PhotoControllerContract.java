package com.gema.photocontroller.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.models.JournalRecord;
import com.gema.photocontroller.models.PlaceForAds;
import com.gema.photocontroller.models.PlacementPlace;
import com.gema.photocontroller.models.Problems;
import com.gema.photocontroller.models.Stations;
import com.gema.photocontroller.models.Wagon;
import com.gema.photocontroller.models.WagonType;

import java.util.ArrayList;

public final class PhotoControllerContract {

    public PhotoControllerContract() {}

    public static final class FilesEntry {

        public final static String TABLE_NAME = "files";

        public final static String COLUMN_RECORD_ID = "record_id";
        public final static String COLUMN_PATH = "path";

        public static ArrayList<String> getAllForID (long id) {

            SQLiteDatabase db = Photocontroler.getDb();
            String[] projection = {
                    COLUMN_PATH};
            String selection = COLUMN_RECORD_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            ArrayList<String> paths = new ArrayList<>();
            try (Cursor cursor = db.query(
                    TABLE_NAME,   // таблица
                    projection,            // столбцы
                    selection,                  // столбцы для условия WHERE
                    selectionArgs,                  // значения для условия WHERE
                    null,                  // Don't group the rows
                    null,                  // Don't filter by row groups
                    null)) {
                int pathColumnIndex = cursor.getColumnIndex(COLUMN_PATH);

                while (cursor.moveToNext()) {
                    paths.add(cursor.getString(pathColumnIndex));
                }
            }
            return paths;
        }

    }

    public static final class JournalEntry implements BaseColumns {

        public final static String TABLE_NAME = "journal";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_IS_SEND = "is_send";
        public final static String COLUMN_TYPE = "type";
        public final static String COLUMN_STATION = "station";
        public final static String COLUMN_PLACEFORADS = "placeforads";
        public final static String COLUMN_PLACEMENTPLACE = "placementplace";
        public final static String COLUMN_WAGON = "wagon";
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
                    COLUMN_PLACEMENTPLACE,
                    COLUMN_WAGON,
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
                while (cursor.moveToNext()) {
                    // Используем индекс для получения строки или числа
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

        public static PlaceForAds getOneEntry(long id) {
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
                    long currentID = cursor.getLong(idColumnIndex);
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
                    long currentID = cursor.getLong(idColumnIndex);
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
                    long currentID = cursor.getLong(idColumnIndex);
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

    /*private PlacementAdv placeForAds;
    private String id;
    private String brandName;
    private String layout;*/
    public static final class PlacementEntry implements BaseColumns {

        public final static String TABLE_NAME = "placement";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_AID = "aid";
        public final static String COLUMN_START_PLACEMENT = "start_placement";
        public final static String COLUMN_STOP_PLACEMENT = "stop_placement";
        public final static String COLUMN_PLACEFORADS = "placeforads";
        public final static String COLUMN_BRANDNAME = "brandname";
        public final static String COLUMN_LAYOUT = "layout";

        public static PlacementPlace getOneEntry(long id) {

            SQLiteDatabase db = Photocontroler.getDb();
            String[] projection = {
                    _ID,
                    COLUMN_AID,
                    COLUMN_START_PLACEMENT,
                    COLUMN_STOP_PLACEMENT,
                    COLUMN_PLACEFORADS,
                    COLUMN_BRANDNAME,
                    COLUMN_LAYOUT };
            String selection = _ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            PlacementPlace placementPlace = null;
            try (Cursor cursor = db.query(
                    TABLE_NAME,   // таблица
                    projection,            // столбцы
                    selection,                  // столбцы для условия WHERE
                    selectionArgs,                  // значения для условия WHERE
                    null,                  // Don't group the rows
                    null,                  // Don't filter by row groups
                    null)) {
                if (cursor.moveToNext()) {
                    placementPlace = new PlacementPlace(cursor);
                }
            }
            return placementPlace;
        }

        public static PlacementPlace getOneEntry(String aid) {

            SQLiteDatabase db = Photocontroler.getDb();
            String[] projection = {
                    _ID,
                    COLUMN_AID,
                    COLUMN_START_PLACEMENT,
                    COLUMN_STOP_PLACEMENT,
                    COLUMN_PLACEFORADS,
                    COLUMN_BRANDNAME,
                    COLUMN_LAYOUT };
            String selection = COLUMN_AID + " = ?";
            String[] selectionArgs = {aid};
            PlacementPlace placementPlace = null;
            try (Cursor cursor = db.query(
                    TABLE_NAME,   // таблица
                    projection,            // столбцы
                    selection,                  // столбцы для условия WHERE
                    selectionArgs,                  // значения для условия WHERE
                    null,                  // Don't group the rows
                    null,                  // Don't filter by row groups
                    null)) {
                if (cursor.moveToNext()) {
                    placementPlace = new PlacementPlace(cursor);
                }
            }
            return placementPlace;
        }

        public static Cursor getAllEntriesCursor() {

            SQLiteDatabase db = Photocontroler.getDb();
            Cursor cursor = null;
            //cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            cursor = db.rawQuery("SELECT placement._id, placement.aid, placement.start_placement, placement.stop_placement, placement.brandname, placement.layout, placeforads.name placeforadsName, placeforads._id placeforadsId FROM placement placement LEFT OUTER JOIN placeforads placeforads ON placement.placeforads = placeforads._id", null);
            return cursor;
        }

        public static Cursor getFilterAidEntries(String filter) {

            SQLiteDatabase db = Photocontroler.getDb();
            Cursor cursor = db.rawQuery("SELECT placement._id, placement.aid, placement.start_placement, placement.stop_placement, placement.brandname, placement.layout, placeforads.name placeforadsName, placeforads._id placeforadsId FROM placement placement LEFT OUTER JOIN placeforads placeforads ON placement.placeforads = placeforads._id WHERE placement.aid like ?" , new String[]{("%" + filter + "%")});
            return cursor;
        }
    }

    public static final class WagonEntry implements BaseColumns {

        public final static String TABLE_NAME = "wagon";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_CODE = "code";
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_NUMBER = "number";
        public final static String COLUMN_WAGON_TYPE = "wagon_type";

        public static Cursor getAllEntriesCursor() {
            SQLiteDatabase db = Photocontroler.getDb();
            Cursor cursor = null;
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            return cursor;
        }

        public static Cursor getFilteredEntriesCursor(String filter) {
            SQLiteDatabase db = Photocontroler.getDb();
            String[] projection = {
                    _ID,
                    COLUMN_CODE,
                    COLUMN_NAME };
            String selection = COLUMN_NAME + " like ?";
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

        public static Wagon getOneEntry(long id) {
            SQLiteDatabase db = Photocontroler.getDb();
            String[] projection = {
                    _ID,
                    COLUMN_CODE,
                    COLUMN_NAME,
                    COLUMN_NUMBER,
                    COLUMN_WAGON_TYPE};
            String selection = _ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            Wagon wagon = null;
            try (Cursor cursor = db.query(
                    TABLE_NAME,   // таблица
                    projection,            // столбцы
                    selection,                  // столбцы для условия WHERE
                    selectionArgs,                  // значения для условия WHERE
                    null,                  // Don't group the rows
                    null,                  // Don't filter by row groups
                    null)) {
                if (cursor.moveToNext()) {
                    wagon = new Wagon(cursor);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return wagon;
        }
    }

    public static final class WagonTypeEntry implements BaseColumns {

        public final static String TABLE_NAME = "wagon_type";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_CODE = "code";
        public final static String COLUMN_NAME = "name";

        public static WagonType getOneEntryCode(String typeCode) {

            SQLiteDatabase db = Photocontroler.getDb();
            String[] projection = {
                    _ID,
                    COLUMN_CODE,
                    COLUMN_NAME };
            String selection = COLUMN_CODE + " = ?";
            String[] selectionArgs = {typeCode};
            WagonType wagonType = null;
            try (Cursor cursor = db.query(
                    TABLE_NAME,   // таблица
                    projection,            // столбцы
                    selection,                  // столбцы для условия WHERE
                    selectionArgs,                  // значения для условия WHERE
                    null,                  // Don't group the rows
                    null,                  // Don't filter by row groups
                    null)) {
                if (cursor.moveToNext()) {
                    wagonType = new WagonType(cursor);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return wagonType;
        }

        public static WagonType getOneEntry(long typeId) {

            SQLiteDatabase db = Photocontroler.getDb();
            String[] projection = {
                    _ID,
                    COLUMN_CODE,
                    COLUMN_NAME };
            String selection = _ID + " = ?";
            String[] selectionArgs = {String.valueOf(typeId)};
            WagonType wagonType = null;
            try (Cursor cursor = db.query(
                    TABLE_NAME,   // таблица
                    projection,            // столбцы
                    selection,                  // столбцы для условия WHERE
                    selectionArgs,                  // значения для условия WHERE
                    null,                  // Don't group the rows
                    null,                  // Don't filter by row groups
                    null)) {
                if (cursor.moveToNext()) {
                    wagonType = new WagonType(cursor);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return wagonType;
        }
    }

}
