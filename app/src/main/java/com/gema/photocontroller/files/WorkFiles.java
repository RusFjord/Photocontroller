package com.gema.photocontroller.files;

import android.content.Context;
import android.util.Log;

import com.gema.photocontroller.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WorkFiles {

    private String filename;

    public WorkFiles(String filename) {
        this.filename = filename;
    }



    protected String ReadFile(Context context) {

        String result = null;
        BufferedReader bufferedReader = null;
        String currentFileName = context.getApplicationInfo().dataDir + File.separatorChar + this.filename;
        try {
            FileReader fileReader = new FileReader(currentFileName);
            if (fileReader != null) {
                bufferedReader = new BufferedReader(fileReader);
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                fileReader.close();
                result = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("file_read", "File " + filename + " not found", e);
        } catch (IOException e) {
            Log.e("file_read", "IO error", e);
        }
        return result;
    }

    protected String WriteFile(Context context, String filename, String data) {
        String error = null;
        BufferedWriter bufferedWriter = null;
        if (data != null && !data.isEmpty()) {
            String currentFileName = context.getApplicationInfo().dataDir + File.separatorChar + filename;
            try {
                FileWriter fileWriter = new FileWriter(currentFileName);
                if (fileWriter != null) {
                    bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(data);
                    bufferedWriter.close();
                    fileWriter.close();
                }

            } catch (Exception e) {
                error = context.getResources().getString(R.string.error_io_write);
            }
        }
        return error;
    }

    protected void setFilename(String filename) {
        this.filename = filename;
    }

}