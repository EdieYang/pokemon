package com.pokepet.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {
    // ftp服务器ip地址
    private static String FTP_ADDRESS;
    // 端口号
    private static int FTP_PORT;
    // 用户名
    private static String FTP_USERNAME;
    // 密码
    private static String FTP_PASSWORD;
    // 图片路径
    private static String FTP_BASEPATH;

    private static boolean uploadFile(String originFileName, InputStream input) {
        System.out.println(FTP_ADDRESS);
        boolean success = false;
        FTPClient ftp = new FTPClient();
        ftp.setControlEncoding("UTF-8");
        try {
            int reply;
            ftp.connect(FTP_ADDRESS, FTP_PORT);// 连接FTP服务器
            ftp.login(FTP_USERNAME, FTP_PASSWORD);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.makeDirectory(FTP_BASEPATH);
            ftp.changeWorkingDirectory(FTP_BASEPATH);
            success = ftp.storeFile(originFileName, input);
            input.close();
            ftp.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }

    public static Boolean uploadFile(String fileName, InputStream inputStream, String ftpAddress, int ftpPort,
            String ftpUserName, String ftpPassWord, String ftpBasePath) {
        FTP_ADDRESS = ftpAddress;
        FTP_PORT = ftpPort;
        FTP_USERNAME = ftpUserName;
        FTP_PASSWORD = ftpPassWord;
        FTP_BASEPATH = ftpBasePath;
        return uploadFile(fileName,inputStream);
    }
}
