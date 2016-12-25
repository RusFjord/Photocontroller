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

    /*public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_IS_SEND = "is_send";
        public final static String COLUMN_TYPE = "type";
        public final static String COLUMN_STATION = "station";
        public final static String COLUMN_WAGON = "wagon";
        public final static String COLUMN_PROBLEM = "problem";
        public final static String COLUMN_COMMENT = "comment";*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        List<String> tables = new ArrayList<>();
        String SQL_CREATE_JOURNAL_TABLE = "CREATE TABLE " + PhotoControllerContract.JournalEntry.TABLE_NAME + " ("
                + PhotoControllerContract.JournalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PhotoControllerContract.JournalEntry.COLUMN_DATE + " TEXT NOT NULL, "
                + PhotoControllerContract.JournalEntry.COLUMN_IS_SEND + " INTEGER NOT NULL DEFAULT 0, "
                + PhotoControllerContract.JournalEntry.COLUMN_TYPE + " TEXT NOT NULL, "
                + PhotoControllerContract.JournalEntry.COLUMN_STATION + " INTEGER NOT NULL DEFAULT 0, "
              //  + PhotoControllerContract.JournalEntry.COLUMN_WAGON + " INTEGER NOT NULL DEFAULT 0, "
                + PhotoControllerContract.JournalEntry.COLUMN_PROBLEM + " INTEGER NOT NULL DEFAULT 0,"
                + PhotoControllerContract.JournalEntry.COLUMN_COMMENT + " TEXT NOT NULL);";
        tables.add(SQL_CREATE_JOURNAL_TABLE);

        String SQL_CREATE_PROBLEMS_TABLE = "CREATE TABLE " + PhotoControllerContract.ProblemsEntry.TABLE_NAME + " ("
                + PhotoControllerContract.ProblemsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PhotoControllerContract.ProblemsEntry.COLUMN_CODE + " TEXT NOT NULL, "
                + PhotoControllerContract.ProblemsEntry.COLUMN_NAME + " TEXT NOT NULL);";
        tables.add(SQL_CREATE_PROBLEMS_TABLE);

        String SQL_CREATE_STATIONS_TABLE = "CREATE TABLE " + PhotoControllerContract.StationsEntry.TABLE_NAME + " ("
                + PhotoControllerContract.StationsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PhotoControllerContract.StationsEntry.COLUMN_CODE + " TEXT NOT NULL, "
                + PhotoControllerContract.StationsEntry.COLUMN_NAME + " TEXT NOT NULL);";
        tables.add(SQL_CREATE_STATIONS_TABLE);

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
