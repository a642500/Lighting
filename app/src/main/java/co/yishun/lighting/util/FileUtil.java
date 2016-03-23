package co.yishun.lighting.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

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
}
