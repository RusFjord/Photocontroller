package com.gema.photocontroller.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = SQLHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "photocontroller.db";

    private static final int DATABASE_VERSION = 1;

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        List<String> tables = new ArrayList<>();
        String SQL_CREATE_JOURNAL_TABLE = "CREATE TABLE IF NOT EXISTS " + PhotoControllerContract.JournalEntry.TABLE_NAME + " ("
                + PhotoControllerContract.JournalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PhotoControllerContract.JournalEntry.COLUMN_DATE + " TEXT NOT NULL, "
                + PhotoControllerContract.JournalEntry.COLUMN_IS_SEND + " INTEGER NOT NULL DEFAULT 0, "
                + PhotoControllerContract.JournalEntry.COLUMN_TYPE + " TEXT NOT NULL, "
                + PhotoControllerContract.JournalEntry.COLUMN_STATION + " INTEGER NOT NULL DEFAULT 0, "
                + PhotoControllerContract.JournalEntry.COLUMN_PLACEFORADS + " INTEGER NOT NULL DEFAULT 0, "
                + PhotoControllerContract.JournalEntry.COLUMN_PLACEMENTPLACE + " INTEGER NOT NULL DEFAULT 0, "
                + PhotoControllerContract.JournalEntry.COLUMN_WAGON + " INTEGER NOT NULL DEFAULT 0, "
                + PhotoControllerContract.JournalEntry.COLUMN_PROBLEM + " INTEGER NOT NULL DEFAULT 0,"
                + PhotoControllerContract.JournalEntry.COLUMN_COMMENT + " TEXT NOT NULL);";
        tables.add(SQL_CREATE_JOURNAL_TABLE);

        String SQL_CREATE_PROBLEMS_TABLE = "CREATE TABLE IF NOT EXISTS " + PhotoControllerContract.ProblemsEntry.TABLE_NAME + " ("
                + PhotoControllerContract.ProblemsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PhotoControllerContract.ProblemsEntry.COLUMN_CODE + " TEXT NOT NULL, "
                + PhotoControllerContract.ProblemsEntry.COLUMN_NAME + " TEXT NOT NULL);";
        tables.add(SQL_CREATE_PROBLEMS_TABLE);

        String SQL_CREATE_STATIONS_TABLE = "CREATE TABLE IF NOT EXISTS " + PhotoControllerContract.StationsEntry.TABLE_NAME + " ("
                + PhotoControllerContract.StationsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PhotoControllerContract.StationsEntry.COLUMN_CODE + " TEXT NOT NULL, "
                + PhotoControllerContract.StationsEntry.COLUMN_NAME + " TEXT NOT NULL);";
        tables.add(SQL_CREATE_STATIONS_TABLE);

        String SQL_CREATE_FILESMD5_TABLE = "CREATE TABLE IF NOT EXISTS " + PhotoControllerContract.FilesMd5Entry.TABLE_NAME + " ("
                + PhotoControllerContract.FilesMd5Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PhotoControllerContract.FilesMd5Entry.COLUMN_FILENAME + " TEXT NOT NULL, "
                + PhotoControllerContract.FilesMd5Entry.COLUMN_MD5 + " TEXT NOT NULL);";
        tables.add(SQL_CREATE_FILESMD5_TABLE);

        String SQL_CREATE_PLACEFORADS_TABLE = "CREATE TABLE IF NOT EXISTS " + PhotoControllerContract.PlaceForAdsEntry.TABLE_NAME + " ("
                + PhotoControllerContract.PlaceForAdsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PhotoControllerContract.PlaceForAdsEntry.COLUMN_CODE + " TEXT NOT NULL, "
                + PhotoControllerContract.PlaceForAdsEntry.COLUMN_NAME + " TEXT NOT NULL);";
        tables.add(SQL_CREATE_PLACEFORADS_TABLE);

        String SQL_CREATE_PLACEFORADS_TABLE_INDEX = "CREATE INDEX IF NOT EXISTS " + PhotoControllerContract.PlaceForAdsEntry.TABLE_NAME  + "_index ON " + PhotoControllerContract.PlaceForAdsEntry.TABLE_NAME + " ("
                + PhotoControllerContract.PlaceForAdsEntry._ID + " , "
                + PhotoControllerContract.PlaceForAdsEntry.COLUMN_CODE + " , "
                + PhotoControllerContract.PlaceForAdsEntry.COLUMN_NAME + ");";
        tables.add(SQL_CREATE_PLACEFORADS_TABLE_INDEX);

        String SQL_CREATE_FILES_TABLE = "CREATE TABLE IF NOT EXISTS " + PhotoControllerContract.FilesEntry.TABLE_NAME  + " ("
                + PhotoControllerContract.FilesEntry.COLUMN_RECORD_ID + " , "
                + PhotoControllerContract.FilesEntry.COLUMN_PATH + ");";
        tables.add(SQL_CREATE_FILES_TABLE);

        String SQL_CREATE_FILES_TABLE_INDEX = "CREATE INDEX IF NOT EXISTS " + PhotoControllerContract.FilesEntry.TABLE_NAME  + "_index ON " + PhotoControllerContract.FilesEntry.TABLE_NAME + " ("
                + PhotoControllerContract.FilesEntry.COLUMN_RECORD_ID + ");";
        tables.add(SQL_CREATE_FILES_TABLE_INDEX);

        String SQL_CREATE_PLACEMENT_TABLE = "CREATE TABLE IF NOT EXISTS " + PhotoControllerContract.PlacementEntry.TABLE_NAME + " ("
                + PhotoControllerContract.PlacementEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PhotoControllerContract.PlacementEntry.COLUMN_AID + " TEXT NOT NULL, "
                + PhotoControllerContract.PlacementEntry.COLUMN_START_PLACEMENT + " TEXT NOT NULL, "
                + PhotoControllerContract.PlacementEntry.COLUMN_STOP_PLACEMENT + " TEXT NOT NULL, "
                + PhotoControllerContract.PlacementEntry.COLUMN_BRANDNAME + " TEXT NOT NULL, "
                + PhotoControllerContract.PlacementEntry.COLUMN_PLACEFORADS + " INTEGER DEFAULT 0, "
                + PhotoControllerContract.PlacementEntry.COLUMN_LAYOUT + " TEXT NOT NULL);";
        tables.add(SQL_CREATE_PLACEMENT_TABLE);

        String SQL_CREATE_PLACEMENT_TABLE_INDEX = "CREATE INDEX IF NOT EXISTS " + PhotoControllerContract.PlacementEntry.TABLE_NAME  + "_index ON " + PhotoControllerContract.PlacementEntry.TABLE_NAME + " ("
                + PhotoControllerContract.PlacementEntry.COLUMN_AID + ");";
        tables.add(SQL_CREATE_PLACEMENT_TABLE_INDEX);

        String SQL_CREATE_WAGON_TABLE = "CREATE TABLE IF NOT EXISTS " + PhotoControllerContract.WagonEntry.TABLE_NAME + " ("
                + PhotoControllerContract.WagonEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PhotoControllerContract.WagonEntry.COLUMN_CODE + " TEXT NOT NULL, "
                + PhotoControllerContract.WagonEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + PhotoControllerContract.WagonEntry.COLUMN_NUMBER + " INTEGER DEFAULT 0, "
                + PhotoControllerContract.WagonEntry.COLUMN_WAGON_TYPE + " INTEGER DEFAULT 0);";
        tables.add(SQL_CREATE_WAGON_TABLE);

        String SQL_CREATE_WAGON_TYPE_TABLE = "CREATE TABLE IF NOT EXISTS " + PhotoControllerContract.WagonTypeEntry.TABLE_NAME + " ("
                + PhotoControllerContract.WagonTypeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PhotoControllerContract.WagonTypeEntry.COLUMN_CODE + " TEXT NOT NULL, "
                + PhotoControllerContract.WagonTypeEntry.COLUMN_NAME + " TEXT NOT NULL);";
        tables.add(SQL_CREATE_WAGON_TYPE_TABLE);

        for (String table : tables) {
            db.execSQL(table);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        List<String> tables = new ArrayList<>();
        while (c.moveToNext()) {
            tables.add(c.getString(0));
        }
        for (String table : tables) {
            String dropQuery = "DROP TABLE IF EXISTS " + table;
            db.execSQL(dropQuery);
        }
        onCreate(db);
    }
}
