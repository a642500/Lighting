package co.yishun.lighting.util;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import co.yishun.lighting.Constants;

/**
 * Created by Carlos on 2016/3/22.
 */
public class FileUtil {
    /**
     * Get database path for WebView.
     *
     * @return path
     */
    public static String getDatabasePath(Context context) {
        return context.getApplicationInfo().dataDir + "/databases/";
    }

    public static File getVideoCacheFile(Context context) {
        return new File(getCacheDirectory(context, false), "video-" + System.currentTimeMillis() + ".mp4");
    }

    public static File getAudioStoreFile(Context context, int order) {
        return new File(getCacheDirectory(context, false), "audioq" + Constants.URL_HYPHEN + order + Constants.AUDIO_FILE_SUFFIX);
    }


    /*
    * for cache normal
    */
    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;

        if (preferExternal && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            appCacheDir = getExternalDirectory(context);
        }

        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }

        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache_short/";
            LogUtil.d(FileUtil.class.getName(), "Can't define system cache directory! use " + cacheDirPath);
            appCacheDir = new File(cacheDirPath);
        }

        return appCacheDir;
    }


    private static File getExternalDirectory(Context context) {

        File cacheDir = context.getExternalCacheDir();
        if (cacheDir != null && !cacheDir.exists()) {
            if (!cacheDir.mkdirs()) {
                LogUtil.d(FileUtil.class.getName(), "无法创建SDCard cache");
                return null;
            }

            //try {
            //    new File(cacheDir, ".nomedia").createNewFile();
            //} catch (IOException e) {
            //    Log.d(FileUtil.class.getName(), "无法创建 .nomedia 文件");
            //}
        }

        return cacheDir;
    }

    public static File getInternalFile(Context context, String filename) {
        return new File(context.getFilesDir(), filename);
    }

    public static boolean unZip(String zipPath, String outputPath) {
        InputStream inputStream = null;
        ZipInputStream zipInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            String filename;
            inputStream = new FileInputStream(zipPath);
            zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));
            ZipEntry zipEntry;
            File entryFile;
            byte[] buffer = new byte[1024];
            int count;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                filename = zipEntry.getName();
                entryFile = new File(outputPath, filename);
                if (zipEntry.isDirectory()) {
                    entryFile.mkdirs();
                    continue;
                }

                if (entryFile.exists()) entryFile.delete();
                fileOutputStream = new FileOutputStream(entryFile);
                while ((count = zipInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, count);
                }
                fileOutputStream.close();
                zipInputStream.closeEntry();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (zipInputStream != null) zipInputStream.close();
                if (fileOutputStream != null) fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
