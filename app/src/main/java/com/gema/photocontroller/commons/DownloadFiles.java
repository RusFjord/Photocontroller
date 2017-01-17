package com.gema.photocontroller.commons;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.gema.photocontroller.commands.AppUpdateCommand;
import com.gema.photocontroller.files.WorkFiles;
import com.gema.photocontroller.interfaces.Command;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class DownloadFiles extends WorkFiles{

    private final String REMOTE_PATH_DATA = "/uploads/app/data/";
    private AppPreference preference;
    private PreferenceData preferenceData;
    private boolean isUpdate = false;

    public DownloadFiles(String path, Context context) {
        super("tasks.json");
        this.preference = new AppPreference(context);
    }

    private ArrayList<String> getListServerFiles() {
        ArrayList<String> result = new ArrayList<>();
        result.add("tasks.zip");
        result.add("placements.zip");
        result.add("placeforads.zip");
        result.add("problems.zip");
        result.add("stations.zip");
        return result;
    }

    @Deprecated
    private void testWrite(Context context) {
        String json = "{" +
                "\"tasks\":[" +
                "{" +
                "\"date\":\"12.11.2015 20:15:00\"," +
                "\"type\":\"Объезд\"," +
                "\"placeforads\":{\"id\":\"1\",\"name\":\"Место 1\"}," +
                "\"layout\":\"Макет 1\"" +
                "}," +
                "{" +
                "\"date\":\"17.11.2015 20:15:00\"," +
                "\"type\":\"Объезд\"," +
                "\"placeforads\":{\"id\":\"2\",\"name\":\"Место 2\"}," +
                "\"layout\":\"Макет 123\"" +
                "}," +
                "{" +
                "\"date\":\"18.11.2015 20:15:00\"," +
                "\"type\":\"Фотоотчет\"," +
                "\"placeforads\":{\"id\":\"24\",\"name\":\"Место 24\"}," +
                "\"layout\":\"Макет 176\"" +
                "}," +
                "{" +
                "\"date\":\"20.11.2015 20:15:00\"," +
                "\"type\":\"Объезд\"," +
                "\"placeforads\":{\"id\":\"62\",\"name\":\"Место 62\"}," +
                "\"layout\":\"Макет 35\"" +
                "}," +
                "{" +
                "\"date\":\"21.11.2015 20:15:00\"," +
                "\"type\":\"Фотоотчет\"," +
                "\"placeforads\":{\"id\":\"217\",\"name\":\"Место 217\"}," +
                "\"layout\":\"Макет 21\"" +
                "}" +
                "]" +
                "}";

        //super.WriteFile(context, "tasks.json", json);
    }

    private File getFile(Context context, SFTPClient sftpClient, String filename) {

        File result = null;
        String remoteFileName = REMOTE_PATH_DATA + filename;
        String localFileName = context.getApplicationInfo().dataDir + File.separatorChar + filename;
        if (sftpClient.exist(remoteFileName)) {
            result = sftpClient.download(localFileName, remoteFileName);
        }
        return result;
    }

    private File getFile(SFTPClient sftpClient, String remoteFileName, String localFileName) {

        File result = null;
        if (sftpClient.exist(remoteFileName)) {
            result = sftpClient.download(localFileName, remoteFileName);
        }
        return result;
    }

    private void preparePreferenceData(Context context, String filename) {
        this.preferenceData = new PreferenceData(filename, context);
    }

    private void tryVersion(Context context, SFTPClient sftpClient, String remoteRoot, String currentVersion) {
        String filenamePreference = "preference.json";
        String remoteFileName = remoteRoot + filenamePreference;
        String localFileName = context.getApplicationInfo().dataDir + File.separatorChar + filenamePreference;
        if (sftpClient.exist(remoteFileName)) {
            sftpClient.download(localFileName, remoteFileName);
            preparePreferenceData(context, filenamePreference);
            if (!currentVersion.equals(this.preferenceData.getVersion())) {
                this.isUpdate = true;
            }
        }
    }

    public boolean tryUpdate(Context context) {
        return this.isUpdate;
    }

    public void getFiles(Context context) {
        AppPreference.RemoteStructure remoteStructure = this.preference.getRemoteSettings();
        SFTPClient sftpClient = new SFTPClient(remoteStructure.getRemoteHost(), remoteStructure.getRemoteLogin(), remoteStructure.getRemotePassword());
        tryVersion(context, sftpClient, remoteStructure.getRemoteRoot(), preference.getStringValue("current_version"));
        if (this.isUpdate) {
            getUpdateFile(context);
        } else {
            ArrayList<String> filenames = getListServerFiles();
            ArrayList<File> files = new ArrayList<>();
            for (String filename : filenames) {
                File returnedFile = getFile(context, sftpClient, filename);
                if (returnedFile != null) {
                    files.add(returnedFile);
                }
            }
            JobWithZip jobWithZip = new JobWithZip();
            File destDir = new File(context.getApplicationInfo().dataDir);
            for (File zipFile : files) {
                String localFileName = zipFile.getAbsolutePath();
                if (localFileName.contains(".zip")) {
                    try {
                        ArrayList<File> unZipFiles = jobWithZip.toUnZip(localFileName, destDir);
                        for (File file : unZipFiles) {
                            Log.i("UnZip", "Файл " + file.getName() + " разархивирован");
                        }
                    } catch (Exception e) {
                        Log.e("UnZip", e.getMessage());
                    }
                }
            }
        }

    }

    private void getUpdateFile(Context context) {
        AppPreference.RemoteStructure remoteStructure = this.preference.getRemoteSettings();
        SFTPClient sftpClient = new SFTPClient(remoteStructure.getRemoteHost(), remoteStructure.getRemoteLogin(), remoteStructure.getRemotePassword());
        String remoteFileName = remoteStructure.getRemoteRoot() + preference.getStringValue("distribution_path") + "photocontroller.apk";
        File localDistributionDir = new File(context.getApplicationInfo().dataDir + File.separatorChar + preference.getStringValue("distribution_path"));
        localDistributionDir.mkdir();
        String localFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + Environment.DIRECTORY_DOWNLOADS + File.separatorChar + "photocontroller.apk";
        File updateFile = getFile(sftpClient, remoteFileName, localFileName);
        updateFile.deleteOnExit();
    }
}