package co.yishun.lighting;

/**
 * Created by Carlos on 2016/3/22.
 */
public class Constants {
    public static final boolean LOG_ENABLE = false;
    public static final boolean SANDBOX = false;

    public static final String MIME_TYPE = "video/mp4";
    public static final String VIDEO_FILE_SUFFIX = ".mp4";
    public static final String THUMB_FILE_SUFFIX = ".png";
    public static final String URL_HYPHEN = "-";

    public static final String IDENTITY_DIR = "identity";
    public static final String IDENTITY_INFO_FILE_NAME = "info";
    public static final String TIME_FORMAT = "yyyyMMdd";
    public static final String TIEM_FORMAT_EXPORT = "yyyyMMdd_HHmmss";
    public static final String PROFILE_PREFIX = "avatar-";
    public static final String PROFILE_SUFFIX = ".png";
    public static final int VIDEO_HEIGHT = 640;
    public static final int VIDEO_WIDTH = 640;
    public static final int VIDEO_FPS = 30;

    public static final String BASE_URL;

    static {
        BASE_URL = SANDBOX ? "http://devlightup.hustonline.net/v1/" : "http://lightup.hustonline.net/v1/";
    }

}
