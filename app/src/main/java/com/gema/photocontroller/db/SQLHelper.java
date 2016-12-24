package com.gema.photocontroller.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        // Строка для создания таблицы
        String SQL_CREATE_JOURNAL_TABLE = "CREATE TABLE " + PhotoControllerContract.JournalEntry.TABLE_NAME + " ("
                + PhotoControllerContract.JournalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PhotoControllerContract.JournalEntry.COLUMN_DATE + " TEXT NOT NULL, "
                + PhotoControllerContract.JournalEntry.COLUMN_IS_SEND + " INTEGER NOT NULL DEFAULT 0, "
                + PhotoControllerContract.JournalEntry.COLUMN_TYPE + " TEXT NOT NULL, "
                + PhotoControllerContract.JournalEntry.COLUMN_STATION + " INTEGER NOT NULL DEFAULT 0, "
                + PhotoControllerContract.JournalEntry.COLUMN_WAGON + " INTEGER NOT NULL DEFAULT 0, "
                + PhotoControllerContract.JournalEntry.COLUMN_PROBLEM + " INTEGER NOT NULL DEFAULT 0,"
                + PhotoControllerContract.JournalEntry.COLUMN_COMMENT + " TEXT NOT NULL);";

        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_JOURNAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
