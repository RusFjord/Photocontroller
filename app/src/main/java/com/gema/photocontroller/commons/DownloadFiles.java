package com.gema.photocontroller.commons;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.gema.photocontroller.files.WorkFiles;

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
    @Deprecated
    private ArrayList<String> getListServerFiles() {
        ArrayList<String> result = new ArrayList<>();
        result.add("tasks.zip");
        result.add("placements.zip");
        result.add("placeforads.zip");
        result.add("problems.zip");
        result.add("stations.zip");
        return result;
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

    private void preparePreferenceData(Context context, SFTPClient sftpClient, String remoteRoot) {
        String filenamePreference = "preference.json";
        String remoteFileName = remoteRoot + filenamePreference;
        String localFileName = context.getApplicationInfo().dataDir + File.separatorChar + filenamePreference;
        if (sftpClient.exist(remoteFileName)) {
            sftpClient.download(localFileName, remoteFileName);
            this.preferenceData = new PreferenceData(filenamePreference, context);
            this.preferenceData.updatePreference(context);
        }
    }

    public boolean tryUpdate() {
        return this.isUpdate;
    }

    public void getFiles(Context context) {
        AppPreference.RemoteStructure remoteStructure = this.preference.getRemoteSettings();
        SFTPClient sftpClient = new SFTPClient(remoteStructure.getRemoteHost(), remoteStructure.getRemoteLogin(), remoteStructure.getRemotePassword());
        ArrayList<File> files = new ArrayList<>();
        try {
            preparePreferenceData(context, sftpClient, remoteStructure.getRemoteRoot());
            ArrayList<String> filenames = this.preferenceData.getFilesForDownload();

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
        } catch (Exception e) {
            Log.e("REMOTE CONNECTION", e.getMessage());
        }
    }
    @Deprecated
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

    public PreferenceData getPreferenceData() {
        return this.preferenceData;
    }
    @Deprecated
    public boolean tryConnection(Context applicationContext) {
        boolean result = false;
        AppPreference.RemoteStructure remoteStructure = this.preference.getRemoteSettings();
        SFTPClient sftpClient = new SFTPClient(remoteStructure.getRemoteHost(), remoteStructure.getRemoteLogin(), remoteStructure.getRemotePassword());
        String filenamePreference = "preference.json";
        String remoteFileName = remoteStructure.getRemoteRoot() + filenamePreference;
        try {
            if (sftpClient.exist(remoteFileName))
                result = true;
        } catch (Exception e) {
            Log.d("INTERNET CONNECTION", "Нет соединения");
        }
        return result;
    }
}