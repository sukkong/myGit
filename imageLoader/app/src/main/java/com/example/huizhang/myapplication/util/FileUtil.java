package com.example.huizhang.myapplication.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.huizhang.myapplication.exception.NoSdCardException;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    private static final String ROOT_PATH = "/Tdx/";
    private static final String PHOTO_PATH = "Camera/";
    private static final String SCREEN_SHOT_PATH = "Screenshots/";
    private static final String QRCODE_PATH = "Qrcode/";
    private static final String CONTACT_PATH = "Contact/";
    private static final String LOG_PATH = "Log/";
    private static final String APK_NAME = "tdx.apk";

    public final static String tmpSuffix = ".tmp";
    public final static char rightparenthesis = ')';
    public final static char leftparenthesis = '(';

    /**
     * 读取文本数据
     *
     * @param context  程序上下文
     * @param fileName 文件名
     * @return String, 读取到的文本内容，失败返回null
     */
    public static String readAssets(Context context, String fileName) {
        InputStream is = null;
        String content = null;
        try {
            is = context.getAssets().open(fileName);
            if (is != null) {

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int readLength = is.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                is.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            content = null;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return content;
    }

    /**
     * sd卡是否卸载
     *
     * @return
     */
    public static boolean isSDCardMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && FileUtil.isFileCanReadAndWrite(Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    /**
     * 得到根目录
     *
     * @throws NoSdCardException
     */
    public static String getRootPath() throws NoSdCardException {
        if (isSDCardMounted()) {
            return Environment.getExternalStorageDirectory().getPath() + ROOT_PATH;
        } else {
            throw new NoSdCardException();
        }
    }

    /**
     * 得到下载apk的路径
     *
     * @return
     * @throws NoSdCardException
     */
    public static String getApkFile() throws NoSdCardException {
        return getRootPath() + APK_NAME;
    }

    /**
     * 得到照片完整路径（包括照片名称）
     *
     * @return
     * @throws NoSdCardException
     */
    public static File getPhotoFile() throws NoSdCardException {
        File storageDir = new File(getRootPath() + PHOTO_PATH);
        try {
            return createImageFile(storageDir, ".jpg");
        } catch (IOException e) {
            throw new NoSdCardException();
        }
    }

    private static File createImageFile(File storageDir, String imageType) throws IOException, NoSdCardException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File albumF = getAlbumDir(storageDir);
        File imageF = File.createTempFile(imageFileName, imageType, albumF);
        return imageF;
    }

    private static File getAlbumDir(File storageDir) throws NoSdCardException {
        if (!storageDir.mkdirs() && !storageDir.exists()) {
            throw new NoSdCardException();
        }
        return storageDir;
    }

    /**
     * 将图片增加到相册
     *
     * @param photoPath
     * @param context
     */
    public static void galleryAddPic(String photoPath, Context context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 得到Qrcode文件夹路径
     *
     * @return
     */
    public static String getQrcodePath() throws NoSdCardException {
        return getRootPath() + QRCODE_PATH;
    }

    /**
     * 得到二维码图片文件
     *
     * @return
     */
    public static String getQrcodeFile() throws NoSdCardException {
        return getQrcodePath() + "Qrcode.jpg";
    }

    /**
     * 得到保存联系人头像目录
     *
     * @return
     */
    public static String getContactPath() throws NoSdCardException {
        return getRootPath() + CONTACT_PATH;
    }

    /**
     * 得到联系人头像文件
     *
     * @return
     * @throws NoSdCardException
     */
    public static String getContactFile() throws NoSdCardException {
        return getContactPath() + System.currentTimeMillis() + ".png";
    }

    /**
     * 得到保存log目录
     *
     * @return
     */
    public static String getLogPath() throws NoSdCardException {
        return getRootPath() + LOG_PATH;
    }

    /**
     * 得到log文件
     *
     * @return
     */
    public static String getLogFile() throws NoSdCardException {
        return getLogPath() + "mj_log.txt";
    }

    /**
     * 是否可读可写
     *
     * @param filePath
     * @return
     */
    public static boolean isFileCanReadAndWrite(String filePath) {
        if (null != filePath && filePath.length() > 0) {
            File f = new File(filePath);
            if (null != f && f.exists()) {
                return f.canRead() && f.canWrite();
            }
        }
        return false;
    }

    /**
     * 修改文件权限
     *
     * @param permission
     * @param path
     */
    public static void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
        }
    }

    /**
     * 新建文件
     *
     * @param fileFullName
     * @return
     * @throws java.io.IOException
     */
    public static void newFile(String fileFullName) throws IOException {
        int pos = StringUtils.getPathLastIndex(fileFullName);
        if (pos > 0) {
            String strFolder = fileFullName.substring(0, pos);
            File file = new File(strFolder);
            if (!file.isDirectory()) {
                file.mkdirs();
            }
        }
        File file = new File(fileFullName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
    }

    /**
     * Prints some data to a file using a BufferedWriter
     */
    public static boolean writeToFile(String filename, String data, boolean append) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(filename, append));
            bufferedWriter.write(data);
            return true;
        } catch (FileNotFoundException ex) {
            Log.e(TAG, "FileNotFoundException", ex);
        } catch (IOException ex) {
            Log.e(TAG, "IOException", ex);
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                Log.e(TAG, "IOException", ex);
            }
        }
        return false;
    }

}
