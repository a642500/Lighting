package co.yishun.lighting;

/**
 * Created by Carlos on 2016/3/22.
 */
public class Constants {
    public static final boolean LOG_ENABLE = false;
    public static final boolean SANDBOX = true;

    public static final String MIME_TYPE = "video/mp4";
    public static final String VIDEO_FILE_SUFFIX = ".mp4";
    public static final String AUDIO_FILE_SUFFIX = ".mp3";
    public static final String THUMB_FILE_SUFFIX = ".png";
    public static final String URL_HYPHEN = "-";

    public static final String IDENTITY_DIR = "identity";
    public static final String IDENTITY_INFO_FILE_NAME = "info";
    public static final String TIME_FORMAT = "yyyy-MM-dd";
    public static final String TIEM_FORMAT_EXPORT = "yyyyMMdd_HHmmss";
    public static final String PROFILE_PREFIX = "avatar-";
    public static final String PROFILE_SUFFIX = ".png";
    public static final int VIDEO_HEIGHT = 640;
    public static final int VIDEO_WIDTH = 640;
    public static final String FILE_URL_PREFIX = "file://";
    public static final String APP_URL_PREFIX = "lightupjs://";

    public static final int VIDEO_FPS = 30;
    public static final String HYBRD_UNZIP_DIR = "hybrd";
    public static final int AUDIO_MAX_DURATION = 60;//s
    public static final int VIDEO_MAX_DURATION = 60;//s


    public static final String BASE_URL;

    static {
        BASE_URL = SANDBOX ? "http://devlightup.yishun.co:61336/v1/" : "http://lightup.yishun.co:61336/v1/";
    }


    public static class Perference {
        public static final String RUNTIME_PREFERENCE = "run_preference";
        public static final String PREFERENCE_IS_FIRST_LAUNCH = "is_first_launch";
        public static final String PREFERENCE_SPLASH_UPDATE_TIME = "splash_update_time";
        public static final String PREFERENCE_SPLASH_COVER_NAME = "splash_cover_name";
        public static final String PREFERENCE_SPLASH_STAY = "splash_stay";
        public static final String PREFERENCE_HYBRID_NAME = "hybrid_name";
        public static final String PREFERENCE_HYBRID_UPDATE_TIME = "hybrid_update_time";
        public static final String PREFERENCE_HYBRID_MD5 = "hybrid_md5";
        public static final String PREFERENCE_HYBRID_LENGTH = "hybrid_length";
        public static final String PREFERENCE_HYBRID_UNZIP_TIME = "hybrd_unzip_time";
        public static final String DEFAULT_SPLASH_COVER_NAME = "splash_cover_0.png";

        public static final String PREFERENCE_TOKEN = "token";
    }
}
