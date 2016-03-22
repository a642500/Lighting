package co.yishun.lighting.util;

import android.content.Context;

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

}
