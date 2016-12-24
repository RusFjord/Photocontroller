package com.gema.photocontroller.db;

import android.provider.BaseColumns;

public class TasksContract {

    private TasksContract() {
    };

    public static final class TasksEntry implements BaseColumns {
        public final static String TABLE_NAME = "tasks";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_TYPE = "type";
        public final static String COLUMN_PLACEFORADS = "placeforads";
        public final static String COLUMN_LAYOUT = "layout";

    }

}
