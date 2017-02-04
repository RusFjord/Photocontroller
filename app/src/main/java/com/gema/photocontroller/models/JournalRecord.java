package com.gema.photocontroller.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gema.photocontroller.application.Photocontroler;
import com.gema.photocontroller.commons.AppPreference;
import com.gema.photocontroller.commons.JobWithZip;
import com.gema.photocontroller.commons.SFTPClient;
import com.gema.photocontroller.db.PhotoControllerContract;
import com.gema.photocontroller.files.WorkFiles;
import com.gema.photocontroller.interfaces.PlacementAdv;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class JournalRecord extends WorkFiles implements Comparable{

    private final String DIRNAME_RESULT = "uploads/app/result/";

    private long id;
    private Date date;
    private PlacementAdv placementAdv;
    private ArrayList<File> files = new ArrayList<>();
    private boolean isSend = false;
    private String type;
    private Stations station;
    private String comment;
    private Problems problem;
    private Wagon wagon;

    private JournalRecord() {
        super("test");
    }

    private JournalRecord(String type) {
        this();
        this.date = new Date();
        this.type = type;
    }

    public JournalRecord(String type, PlacementAdv placementAdv, File photo) {
        this(type);
        this.placementAdv = placementAdv;
        this.files.add(photo);
    }

    public JournalRecord(String type, PlacementAdv placementAdv, Wagon wagon, File photo) {
        this(type);
        this.placementAdv = placementAdv;
        this.wagon = wagon;
        this.files.add(photo);
    }

    public JournalRecord(String type, PlacementAdv placementAdv, File photo, Problems problem) {
        this(type);
        this.placementAdv = placementAdv;
        this.files.add(photo);
        this.problem = problem;
    }

    public JournalRecord(String type, ArrayList<File> photos, Stations station) {
        this(type);
        this.station = station;
        this.files.addAll(photos);
    }

    public JournalRecord(Cursor cursor) {
        this();
        int idColumnIndex = cursor.getColumnIndex(PhotoControllerContract.JournalEntry._ID);
        int dateColumnIndex = cursor.getColumnIndex(PhotoControllerContract.JournalEntry.COLUMN_DATE);
        int issendColumnIndex = cursor.getColumnIndex(PhotoControllerContract.JournalEntry.COLUMN_IS_SEND);
        int typeColumnIndex = cursor.getColumnIndex(PhotoControllerContract.JournalEntry.COLUMN_TYPE);
        int placeforadsColumnIndex = cursor.getColumnIndex(PhotoControllerContract.JournalEntry.COLUMN_PLACEFORADS);
        int problemColumnIndex = cursor.getColumnIndex(PhotoControllerContract.JournalEntry.COLUMN_PROBLEM);
        int stationColumnIndex = cursor.getColumnIndex(PhotoControllerContract.JournalEntry.COLUMN_STATION);
        int commentColumnIndex = cursor.getColumnIndex(PhotoControllerContract.JournalEntry.COLUMN_COMMENT);

        this.id = cursor.getLong(idColumnIndex);
        this.comment = cursor.getString(commentColumnIndex);
        this.type = cursor.getString(typeColumnIndex);
        int isSendInt = cursor.getInt(issendColumnIndex);
        this.isSend = (isSendInt != 0);
        String dateString = cursor.getString(dateColumnIndex);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS", Locale.getDefault());
        try {
            this.date = dateFormat.parse(dateString);
        } catch (Exception e) {
            Log.e("JOURNAL REC DATE FORMAT", "Ошибка форматирования даты");
        }
        this.placementAdv = PhotoControllerContract.PlaceForAdsEntry.getOneEntry(cursor.getInt(placeforadsColumnIndex));
        this.problem = PhotoControllerContract.ProblemsEntry.getOneEntry(cursor.getInt(problemColumnIndex));
        this.station = PhotoControllerContract.StationsEntry.getOneEntry(cursor.getInt(stationColumnIndex));

        ArrayList<String> paths = PhotoControllerContract.FilesEntry.getAllForID(this.id);
        for (String path : paths) {
            this.files.add(new File(path));
        }

    }

    public JSONObject getJSON() {
        JSONObject recordJSON = new JSONObject();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
            recordJSON.put("date", dateFormat.format(this.date));
            recordJSON.put("type", this.type);
            if (this.placementAdv != null) {
                JSONObject placeForAdsJSON = this.placementAdv.getJSON();
                recordJSON.put("placeforads", placeForAdsJSON);
            }
            if (this.problem != null) {
                JSONObject placeForAdsJSON = this.problem.getJSON();
                recordJSON.put("problem", placeForAdsJSON);
            }
            if (this.station != null) {
                JSONObject stationsJSON = this.station.getJSON();
                recordJSON.put("station", stationsJSON);
            }
            if (this.wagon != null) {
                JSONObject wagonJSON = this.wagon.getJSON();
                recordJSON.put("wagon", wagonJSON);
            }
            recordJSON.put("comment", this.comment);
            recordJSON.put("isSend", this.isSend);
            JSONArray filesJSON = new JSONArray();
            for (File file : this.files) {
                JSONObject path = new JSONObject();
                path.put("path", file.getAbsolutePath());
                filesJSON.put(path);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return recordJSON;
    }

    public long getId() {
        return this.id;
    }

    public Date getDate() {
        return this.date;
    }

    public PlacementAdv getPlacementAdv() {
        return this.placementAdv;
    }

    public Stations getStation() {
        return this.station;
    }

    public String getType() {
        return this.type;
    }

    public String getComment() {
        return this.comment;
    }

    public boolean getSendState() { return this.isSend; }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int compareTo(Object o) {

        JournalRecord otherRecord = (JournalRecord) o;
        return (int) (otherRecord.getDate().getTime() - this.date.getTime());
    }

    public void send(Context context) {

        String dirPath = context.getApplicationInfo().dataDir + "/send/";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        final AppPreference preference = new AppPreference(context);
        String filenamePart  = getTempFilename(preference, ".zip");
        String filenameRec  = getTempFilename(preference, ".json");
        String localFileName = dir.getAbsolutePath() + File.separatorChar + filenamePart;

        this.WriteFile(context, "/send/" + filenameRec, getJSON().toString());
        String localFileNameRec = dir.getAbsolutePath() + File.separatorChar + filenameRec;
        File file = new File(localFileNameRec);
        ArrayList<File> listToZip = new ArrayList<>();
        listToZip.add(file);
        listToZip.addAll(this.files);
        JobWithZip jobWithZip = new JobWithZip();
        try {
            File zipFile = jobWithZip.toZip(listToZip, localFileName);
            Log.i("Zip", "Файл " + zipFile.getName() + " создан");
        } catch (Exception e) {
            Log.e("Zip", e.getMessage());
        }

        final AppPreference.RemoteStructure remoteStructure = preference.getRemoteSettings();
        SFTPClient sftpClient = new SFTPClient(remoteStructure.getRemoteHost(), remoteStructure.getRemoteLogin(), remoteStructure.getRemotePassword());
        String remoteFilePath = DIRNAME_RESULT + filenamePart;
        try {
            sftpClient.upload(new File(localFileName), remoteFilePath);
            this.isSend = true;
            SQLiteDatabase db = Photocontroler.getDb();
            ContentValues contentValues = this.getContentValues(false);
            db.replace(PhotoControllerContract.JournalEntry.TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            Log.e("SEND JOURNALRECORD", e.getMessage());
        }
        this.deleteTempFiles(dir);
    }

    private void deleteTempFiles(File tempDir) {

        if (this.isSend) {
            if (tempDir.isDirectory()) {
                File[] tempFiles = tempDir.listFiles();
                for (File file : tempFiles) {
                    file.delete();
                }
            }
        }
    }

    private String getFormatDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS", Locale.getDefault());
        return dateFormat.format(this.date);
    }

    private String getTempFilename(AppPreference preference, String extention) {

        String result = "user-" + preference.getStringValue("device_imei") + "-" + getFormatDate() +  extention;
        return result;
    }

    private ContentValues getContentValues(boolean isNew) {

        ContentValues currentEntry = new ContentValues();

        currentEntry.put("_id", isNew ? null : this.id);
        currentEntry.put("type", this.type);
        currentEntry.put("date", getFormatDate());
        String comment = this.comment == null ? "" : this.comment;
        currentEntry.put("comment", comment);
        currentEntry.put("is_send", this.isSend);
        int stationId = this.station == null ? 0 : this.station.getId();
        currentEntry.put("station", stationId);
        long placeforadsId = this.placementAdv == null ? 0 : this.placementAdv.getId();
        currentEntry.put("placeforads", placeforadsId);
        int problemId = this.problem == null ? 0 : this.problem.getId();
        currentEntry.put("problem", problemId);

        return currentEntry;
    }

    public void add(boolean isNew) {
        SQLiteDatabase db = Photocontroler.getDb();
        ContentValues currentEntry = getContentValues(isNew);
        long currentId = db.replace(PhotoControllerContract.JournalEntry.TABLE_NAME, null, currentEntry);
        if (currentId > 0) {
            ContentValues currentFileEntry = new ContentValues();
            for (File file : this.files) {
                currentFileEntry.clear();
                currentFileEntry.put(PhotoControllerContract.FilesEntry.COLUMN_RECORD_ID, String.valueOf(currentId));
                currentFileEntry.put(PhotoControllerContract.FilesEntry.COLUMN_PATH, file.getAbsolutePath());
                db.insert(PhotoControllerContract.FilesEntry.TABLE_NAME, null, currentFileEntry);
            }
        }
    }
}
