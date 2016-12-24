package com.gema.photocontroller.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class JobWithZip {

    private byte[] buffer = new byte[1024];

    public File toZip(ArrayList<File> files, String filePath) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
        for (File inputFile:
                files) {
            ZipEntry zipEntry = new ZipEntry(inputFile.getName());
            zipOutputStream.putNextEntry(zipEntry);
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            int bytesRead;
            while ((bytesRead = fileInputStream.read(this.buffer)) > 0) {
                zipOutputStream.write(this.buffer, 0, bytesRead);
            }
            zipOutputStream.closeEntry();
        }
        zipOutputStream.close();
        fileOutputStream.close();
        File result = new File(filePath);
        return result;
    }

    public ArrayList<File> toUnZip(String filePath, File directoryDest) throws Exception {
        ArrayList<File> files = new ArrayList<>();
        if (!directoryDest.exists()) {
            directoryDest.mkdir();
        }
        FileInputStream fileInputStream = new FileInputStream(new File(filePath));
        ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
        ZipEntry zipEntry = null; //zipInputStream.getNextEntry();
        int bytesRead;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {

            File newFile = new File(directoryDest, zipEntry.getName());
            if (newFile.exists()) {
                newFile.delete();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            while ((bytesRead = zipInputStream.read(this.buffer, 0, 1024)) > -1) {
                fileOutputStream.write(this.buffer, 0, bytesRead);
            }
            files.add(newFile);
            fileOutputStream.close();
            zipInputStream.closeEntry();
            //zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
        fileInputStream.close();

        return files;
    }

}
