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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class JournalRecord extends WorkFiles implements Comparable{

    private final String DIRNAME_RESULT = "uploads/app/result/";

    private int id;
    private Date date;
    private PlaceForAds placeForAds;
    private ArrayList<File> files = new ArrayList<>();
    private boolean isSend = false;
    private String type;
    private Stations station;
    private String comment;
    private Problems problem;

    private JournalRecord() {
        super("test");
    }

    private JournalRecord(String type) {
        this();
        this.date = new Date();
        this.type = type;
    }

    public JournalRecord(String type, PlaceForAds placeForAds, File photo) {
        this(type);
        this.placeForAds = placeForAds;
        this.files.add(photo);
    }

    public JournalRecord(String type, PlaceForAds placeForAds, File photo, Problems problem) {
        this(type);
        this.placeForAds = placeForAds;
        this.files.add(photo);
        this.problem = problem;
    }

    public JournalRecord(String type, ArrayList<File> photos, Stations station) {
        this(type);
        this.station = station;
        this.files.addAll(photos);
    }

    public JournalRecord(JSONObject jsonObject) {
        this();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
            this.date = dateFormat.parse(jsonObject.getString("date"));
            this.type = jsonObject.getString("type");
            if (jsonObject.has("placeforads")) {
                JSONObject placeForAdsJSON = jsonObject.getJSONObject("placeforads");
                this.placeForAds = new PlaceForAds(placeForAdsJSON);
            }
            if (jsonObject.has("station")) {
                JSONObject stationsJSON = jsonObject.getJSONObject("station");
                this.station = new Stations(stationsJSON);
            }
            if (jsonObject.has("problem")) {
                JSONObject problemJSON = jsonObject.getJSONObject("problem");
                this.problem = new Problems(problemJSON);
            }
            if (jsonObject.has("comment")) {
                this.comment = jsonObject.getString("comment");
            }
            this.isSend = jsonObject.getBoolean("isSend");
            JSONArray filesJSON = jsonObject.getJSONArray("files");
            for (int i = 0; i < filesJSON.length(); i++) {
                JSONObject fileJSON = filesJSON.getJSONObject(i);
                File file = new File(fileJSON.getString("path"));
                this.files.add(file);
            }
        } catch (Exception e) {
            Log.e("JOURNAL RECORD", e.getMessage());
        }
    }

    public JournalRecord(Cursor cursor) {
        this();
        //TODO: Реализовать конструктор
    }

    public JSONObject getJSON() {
        JSONObject recordJSON = new JSONObject();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
            recordJSON.put("date", dateFormat.format(this.date));
            recordJSON.put("type", this.type);
            if (this.placeForAds != null) {
                JSONObject placeForAdsJSON = this.placeForAds.getJSON();
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
            recordJSON.put("comment", this.comment);
            recordJSON.put("isSend", this.isSend);
            JSONArray filesJSON = new JSONArray();
            for (File file : this.files) {
                JSONObject path = new JSONObject();
                path.put("path", file.getAbsolutePath());
                filesJSON.put(path);
            }
            recordJSON.put("files", filesJSON);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return recordJSON;
    }

    public int getId() {
        return this.id;
    }

    public Date getDate() {
        return this.date;
    }

    public PlaceForAds getPlaceForAds() {
        return this.placeForAds;
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
        //String filenamePart  = "user-" + preference.getStringValue("device_imei") + "-" + getFormatDate() +  ".zip";
        String filenamePart  = getTempFilename(preference, ".zip");
        //String filenameRec  = "user-" + preference.getStringValue("device_imei") + "-" + getFormatDate() +  ".json";
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
    /*private int id;
    private Date date;
    private PlaceForAds placeForAds;
    private ArrayList<File> files = new ArrayList<>();
    private boolean isSend = false;
    private String type;
    private Stations station;
    private String comment;
    private Problems problem;*/
    private ContentValues getContentValues() {

        ContentValues currentEntry = new ContentValues();
        currentEntry.put("id", this.id);
        currentEntry.put("type", this.type);
        currentEntry.put("date", getFormatDate());
        currentEntry.put("comment", this.comment);
        currentEntry.put("is_send", this.isSend);
        int stationId = this.station == null ? 0 : this.station.getId();
        currentEntry.put("station", stationId);
        int placeforadsId = this.placeForAds == null ? 0 : this.placeForAds.getId();
        currentEntry.put("placeforads", placeforadsId);
        int problemId = this.problem == null ? 0 : this.problem.getId();
        currentEntry.put("problem", problemId);

        return currentEntry;
    }

    public void add() {
        SQLiteDatabase db = Photocontroler.getDb();
        ContentValues currentEntry = getContentValues();
        db.replace(PhotoControllerContract.JournalEntry.TABLE_NAME, null, currentEntry);
    }
}
