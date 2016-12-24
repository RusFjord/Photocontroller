package com.gema.photocontroller.db;

import android.provider.BaseColumns;

public final class PhotoControllerContract {

    public PhotoControllerContract() {}

    /*private Date date;
    private PlaceForAds placeForAds;
    //private ArrayList<File> files = new ArrayList<>();
    private boolean isSend = false;
    private String type;
    private Stations station;
    private String comment;
    private Problems problem;*/

    public static final class JournalEntry implements BaseColumns {
        public final static String TABLE_NAME = "journal";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_IS_SEND = "is_send";
        public final static String COLUMN_TYPE = "type";
        public final static String COLUMN_STATION = "station";
        public final static String COLUMN_WAGON = "wagon";
        public final static String COLUMN_PROBLEM = "problem";
        public final static String COLUMN_COMMENT = "comment";


    }

}
