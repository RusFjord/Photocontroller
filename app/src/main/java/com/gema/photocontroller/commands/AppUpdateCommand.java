package com.gema.photocontroller.commands;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.gema.photocontroller.commons.AppPreference;
import com.gema.photocontroller.interfaces.Command;

import java.io.File;

public class AppUpdateCommand implements Command{
    @Override
    public void execute(Context context) {
        File file = getUpdateFile(context);
        if (file == null) {
            Log.e("APP UPDATE", "Отсутствует файл обновлений.");
            return;
        }
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    private File getUpdateFile(Context context) {
        String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + Environment.DIRECTORY_DOWNLOADS + File.separatorChar + "photocontroller.apk";
        File file = new File(filename);
        return file;
    }
}
