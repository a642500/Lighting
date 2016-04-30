package co.yishun.lighting.ui.common;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import co.yishun.lighting.Constants;
import co.yishun.lighting.ui.ShootActivity_;
import co.yishun.lighting.ui.UserInfoActivity_;
import co.yishun.lighting.util.LogUtil;


/**
 * Created by Jinge on 2016/1/23.
 */
public abstract class HybridUrlHandler {
    public static final String URL_PREFIX = Constants.APP_URL_PREFIX;
    public static final String FUNC_GET_ACCOUNT = "getAccount";
    //    public static final String FUNC_GET_ACCOUNT_ID = "getAccountId";
    public static final String FUNC_JUMP = "jump";
    public static final String FUNC_LOG = "log";
    public static final String FUNC_GET_OTHERS = "getOthers";
    public static final String FUNC_GET_ACCESS_TOKEN = "getAccessToken";
    public static final String FUNC_CONFIG = "config";
    private static final String TAG = "HybridUrlHandler";


//    public static final String FUNC_ALERT = "alert";
//    public static final String FUNC_CANCEL_AlERT = "cancelAlert";
//    public static final String FUNC_FINISH = "finish";
//    public static final String FUNC_GET_ENV = "getEnv";
//    public static final String FUNC_LOAD = "load";
//    public static final String FUNC_GET_BASIC_AUTH_HEADER = "getBasicAuthHeader";

    public static String urlDecode(String raw) {
        String result;
        try {
            result = URLDecoder.decode(raw, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            result = "decoder error";
            LogUtil.e(TAG, "decoder error : " + raw);
        }
        return result;
    }

    public static List<String> urlDecode(List<String> rawStrings) {
        List<String> results = new ArrayList<>();
        for (String s : rawStrings) results.add(urlDecode(s));
        return results;
    }

    protected abstract boolean handleInnerUrl(UrlModel urlModel);

    public boolean handleUrl(Context activity, String url) {
        return handleUrl(activity, url, 0, 0);
    }

    /**
     * This can be used in hybrd Activities and fragment, and also can be used with other kind of
     * url, such as when click a banner, jump to a certain world.
     *
     * @param url  : use the same format as hybrd.
     * @param posX : the X position for transition. For example, when you want start {@link
     *             ShootActivity_}, you need to specify {@param posX} and {@param posY}, or the
     *             {@link ShootActivity_} with start from the center of the screen.
     * @return True, if we handle the url.
     */
    public boolean handleUrl(Context context, String url, int posX, int posY) {
        if (url.startsWith(URL_PREFIX)) {
            String json = url.substring(URL_PREFIX.length());

            UrlModel urlModel = new UrlModel();

            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

            if (jsonObject.has("type")) {
                urlModel.type = jsonObject.get("type").getAsString();
            }
            if (jsonObject.has("call")) {
                urlModel.call = jsonObject.get("call").getAsString();
            }

            if (jsonObject.has("args")) {
                urlModel.args = new HashMap<>();
                Set<Map.Entry<String, JsonElement>> argMap = jsonObject.getAsJsonObject("args").entrySet();
                for (Map.Entry<String, JsonElement> entry : argMap) {
                    String value = entry.getValue().toString();

                    value = value.replace("\\n", "\n");
                    // Gson escape every splash, so restore newline
                    if (value.startsWith("\"") && value.endsWith("\""))
                        value = value.substring(1, value.length() - 1);

                    urlModel.args.put(entry.getKey(), value);
                }
            }

            if (TextUtils.equals(urlModel.type, FUNC_JUMP)) {
                return webJumpWithPosition(context, urlModel.args, posX, posY);
            }
            return handleInnerUrl(urlModel);
        }
        return false;
    }

    private boolean webJumpWithPosition(Context context, Map<String, String> args, int posX, int posY) {
        String des = args.get("page");
        if (des == null) {
            return false;
        }

        if (TextUtils.equals(des, "update_profile")) {
            UserInfoActivity_.intent(context).start();
        } else if (TextUtils.equals(des, "play_video")) {

        } else if (TextUtils.equals(des, "play_audio")) {

        } else if (TextUtils.equals(des, "other_profile")) {

        } else if (TextUtils.equals(des, "audio_question_list")) {

        } else if (TextUtils.equals(des, "video_question_list")) {

        }
        return true;
    }

    class UrlModel {
        String type;
        String call;
        Map<String, String> args;
    }

}
