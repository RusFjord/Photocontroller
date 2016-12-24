package com.gema.photocontroller.commons;

import android.content.Context;
import android.util.Log;

import com.gema.photocontroller.files.WorkFiles;

import java.io.File;
import java.util.ArrayList;

public class DownloadFiles extends WorkFiles{

    private final String REMOTE_PATH_DATA = "/uploads/app/data/";
    private AppPreference preference;

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

    public void getFiles(Context context) {

//        String server = "trk-media.ru";
//        String user = "xml_sftp";
//        String pass = "cpu2800";
        AppPreference.RemoteStructure remoteStructure = this.preference.getRemoteSettings();

        ArrayList<String> filenames = getListServerFiles();
        ArrayList<File> files = new ArrayList<>();

        SFTPClient sftpClient = new SFTPClient(remoteStructure.getRemoteHost(), remoteStructure.getRemoteLogin(), remoteStructure.getRemotePassword());
        for (String filename: filenames) {
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