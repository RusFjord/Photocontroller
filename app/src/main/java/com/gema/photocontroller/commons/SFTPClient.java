package com.gema.photocontroller.commons;

import android.util.Log;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

import java.io.File;

public class SFTPClient {

    private String hostName;
    private String username;
    private String password;


    public SFTPClient(String hostName, String username, String password) {
        this.hostName = hostName;
        this.username = username;
        this.password = password;
    }

    // Method to upload a file in Remote server
    public void upload(File file, String remoteFilePath) {

        if (!file.exists()) {
            throw new RuntimeException("Error. Local file not found");
        }

        StandardFileSystemManager manager = new StandardFileSystemManager();

        try {
            manager.init();

            // Create local file object
            FileObject localFile = manager.resolveFile(file.getAbsolutePath());

            // Create remote file object
            FileObject remoteFile = manager.resolveFile(
                    createConnectionString(hostName, username, password,
                            remoteFilePath), createDefaultOptions());

            // Copy local file to sftp server
            remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);

            System.out.println("File upload success");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            manager.close();
        }
    }

    // Download file function:
    public File download(String localFilePath, String remoteFilePath) {

        StandardFileSystemManager manager = new StandardFileSystemManager();
        File result = null;
        try {
            manager.init();

            // Create local file object
            FileObject localFile = manager.resolveFile(localFilePath);
            if (localFile.exists()) {
                localFile.delete();
            }

            // Create remote file object
            FileObject remoteFile = manager.resolveFile(
                    createConnectionString(hostName, username, password,
                            remoteFilePath), createDefaultOptions());

            // Copy local file to sftp server
            localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);
            File file = new File(localFilePath);
            if (file.exists()) {
                result = file;
            }
            System.out.println("File download success");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            manager.close();
        }
        return result;
    }

    // Delete file in remote system:
    public void delete(String hostName, String username,
                              String password, String remoteFilePath) {
        StandardFileSystemManager manager = new StandardFileSystemManager();

        try {
            manager.init();

            // Create remote object
            FileObject remoteFile = manager.resolveFile(
                    createConnectionString(hostName, username, password,
                            remoteFilePath), createDefaultOptions());

            if (remoteFile.exists()) {
                remoteFile.delete();
                System.out.println("Delete remote file success");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            manager.close();
        }
    }

    // Check remote file is exist function:
    public boolean exist(String remoteFilePath) {
        StandardFileSystemManager manager = new StandardFileSystemManager();

        try {
            manager.init();

            // Create remote object
            FileObject remoteFile = manager.resolveFile(
                    createConnectionString(hostName, username, password,
                            remoteFilePath), createDefaultOptions());

            System.out.println("File exist: " + remoteFile.exists());

            return remoteFile.exists();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            manager.close();
        }
    }

    // Establishing connection
    private String createConnectionString(String hostName,
                                                String username, String password, String remoteFilePath) {
        return "sftp://" + username + ":" + password + "@" + hostName + ":22/" + remoteFilePath;
    }
    // sftp://xml_sftp:cpu2800@trk-media.ru/
    //  Method to setup default SFTP config:
    private FileSystemOptions createDefaultOptions()
            throws FileSystemException {
        // Create SFTP options
        FileSystemOptions opts = new FileSystemOptions();
        String ds = SftpFileSystemConfigBuilder.getInstance().getStrictHostKeyChecking(opts);
        // SSH Key checking
        SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
                opts, "no");


        // Root directory set to user home
        SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);

        // Timeout is count by Milliseconds
        SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

        return opts;
    }

}
