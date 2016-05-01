package co.yishun.lighting.ui.common;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import co.yishun.lighting.Constants;
import co.yishun.lighting.R;
import co.yishun.lighting.account.AccountManager;
import co.yishun.lighting.api.APIFactory;
import co.yishun.lighting.api.Procedure;
import co.yishun.lighting.api.model.OtherUser;
import co.yishun.lighting.ui.QuestionFragment_;
import co.yishun.lighting.ui.ShootActivity_;
import co.yishun.lighting.ui.UserInfoActivity_;
import co.yishun.lighting.util.FileUtil;
import co.yishun.lighting.util.GsonFactory;
import co.yishun.lighting.util.LogUtil;
import retrofit2.Response;


/**
 * Created by Jinge on 2016/1/21.
 */
@EFragment(R.layout.fragment_web_view)
public class BaseWebFragment extends BaseFragment {
    public static final String URL_PREFIX = Constants.APP_URL_PREFIX;
    public static final String FUNC_GET_ACCOUNT = "getAccount";
    public static final String FUNC_JUMP = "jump";
    public static final String FUNC_LOG = "log";
    public static final String FUNC_GET_OTHERS = "getOthers";
    public static final String FUNC_GET_ACCESS_TOKEN = "getAccessToken";
    public static final String FUNC_CONFIG = "config";
    private static final String TAG = "BaseWebFragment";
    private static boolean needGlobalRefresh = false;
    @ViewById
    protected SwipeRefreshLayout swipeRefreshLayout;
    @ViewById(R.id.webView)
    protected WebView mWebView;
    protected MaterialDialog dialog;
    protected File mHybridDir;
    protected int posX;
    protected int posY;
    protected float touchX;
    protected float touchY;
    protected boolean mRefreshable;
    @FragmentArg
    protected String mUrl;
    @FragmentArg
    protected String mArg;

    @SuppressWarnings("unused")
    public static void invalidateWeb() {
        needGlobalRefresh = true;
    }

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

    @SuppressWarnings("unused")
    public static List<String> urlDecode(List<String> rawStrings) {
        List<String> results = new ArrayList<>();
        for (String s : rawStrings) results.add(urlDecode(s));
        return results;
    }

    @SuppressWarnings("unused")
    public void setRefreshable(boolean refreshable) {
        mRefreshable = refreshable;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRefreshable && needGlobalRefresh) {
            needGlobalRefresh = false;
            reload();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.clearCache(false);
        }
    }

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @AfterInject
    void setDefault() {
        safelyDoWithContext(context -> {
            mHybridDir = FileUtil.getInternalFile(context, Constants.HYBRD_UNZIP_DIR);
            if (TextUtils.isEmpty(mUrl)) {
                mUrl = Constants.FILE_URL_PREFIX +
                        new File(mHybridDir, "build/pages/world/world.html").getPath();
                LogUtil.i(TAG, "url is null, load default");
            }

            int lastUpdateTime = context.getSharedPreferences(Constants.Perference.RUNTIME_PREFERENCE,
                    Context.MODE_PRIVATE).getInt(Constants.Perference.PREFERENCE_HYBRID_UPDATE_TIME, 0);
            if (mUrl.startsWith(Constants.FILE_URL_PREFIX)) mUrl += "?time=" + lastUpdateTime;
        });

    }

    @CallSuper
    @AfterViews
    @SuppressLint("SetJavaScriptEnabled")
    protected void setUpWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mWebView.getSettings().setDatabasePath(FileUtil.getDatabasePath(getContext()));
        }

        mWebView.setWebViewClient(new BaseWebClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getContext().getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
        mWebView.setOnTouchListener((v, event) -> {
            touchX = event.getX();
            touchY = event.getY();
            return false;
        });
        mWebView.post(() -> {
            int location[] = new int[2];
            mWebView.getLocationOnScreen(location);
            posX = location[0];
            posY = location[1];
        });
        mWebView.loadUrl(mUrl);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (mRefreshable) reload();
        });
    }

    public void reload() {
        if (mWebView != null)
            mWebView.reload();
    }

    private void loadOver() {
        swipeRefreshLayout.setRefreshing(false);
    }

//    private void webAlert(Map<String, String> args) {
//        String type = args.get(0);
//        if (dialog != null && dialog.isShowing()) dialog.hide();
//        if (TextUtils.equals(type, "alert")) {
//            dialog = new MaterialDialog.Builder(getContext()).theme(Theme.LIGHT)
//                    .content(args.get(1)).progress(true, 0).build();
//            dialog.show();
//        } else if (TextUtils.equals(type, "message") && mActivity != null) {
//            mActivity.showSnackMsg(args.get(1));
//        } else {
//            LogUtil.e(TAG, "unhandled alert type");
//        }
//    }

//    private void webCancelAlert(Map<String, String> args) {
//        if (dialog != null && dialog.isShowing()) {
//            dialog.hide();
//        }
//    }

//    private void webFinish(Map<String, String> args) {
//        String type = args.get(0);
//        if (TextUtils.equals(type, "preview")) {
////            Intent intent = new Intent();
////            intent.putExtra(PersonalWorldActivity.KEY_NAME, args.get(1));
////            intent.putExtra(PersonalWorldActivity.KEY_ID, args.get(2));
////            if (mActivity != null)
////                mActivity.setResult(PersonalWorldActivity.RESULT_OK, intent);
//        } else {
//            LogUtil.e(TAG, "unhandled finish type");
//        }
//        if (mActivity != null)
//            mActivity.finish();
//    }

//    private void webLoad(Map<String, String> args) {
//        mWebView.loadUrl(String.format(toJs(mArg, false), HybridUrlHandler.FUNC_LOAD));
//    }

//    private void webAuth(Map<String,String args) {
//        mWebView.loadUrl(String.format(toJs(OneMomentClientV4.getAuthStr()),
//                HybridUrlHandler.FUNC_GET_BASIC_AUTH_HEADER));
//    }

    @UiThread
    void doWithWebViewInUIThread(WebViewCallable callable) {
        if (mWebView != null) {
            callable.call(mWebView);
        }
    }

    @Background
    void webGetAccount(String call) {
        safelyDoWithContext(context -> mWebView.loadUrl(
                toJs(AccountManager.getUserInfo(context), true, true, call)));
    }

    private void webLog(Map<String, String> args) {
        LogUtil.i(TAG, "js log : " + args.get("text"));
    }

    private void webConfig(Map<String, String> args) {
        boolean h = Boolean.valueOf(args.get("h_scroll_indicator"));
        mWebView.setHorizontalScrollBarEnabled(h);
        boolean v = Boolean.valueOf(args.get("v_scroll_indicator"));
        mWebView.setVerticalScrollBarEnabled(v);
    }

    @Background
    void webGetAccessToken(Map<String, String> args, String call) {
        String id = args.get("user_id");
        //TODO
        safelyDoWithToken((token) -> doWithWebViewInUIThread(
                webView -> mWebView.loadUrl(toJs(token, false, true, call))));
    }

    @Background
    void webGetOthers(String call) {
        safelyDoWithContextToken((context, token) -> {
            String sexuality = AccountManager.getUserInfo(context).getSexuality().toString();
            //noinspection WrongConstant
            Response<OtherUser> response = APIFactory.getProcedureAPI().
                    getAUser(token.userId, token.accessToken, sexuality).execute();

            if (response.isSuccessful()) {
                OtherUser otherUser = response.body();
                otherUser.userId = token.userId;
                otherUser.accessToken = token.accessToken;
                doWithWebViewInUIThread(webView ->
                        mWebView.loadUrl(toJs(otherUser, false, true, call)));
            } else {
                showSnackMsg(R.string.error_server);
            }
        });
    }

    private String toJs(Object o, boolean encode, boolean naming, Object... stringArgs) {
        String arg;
        Gson gson;
        if (naming)
            gson = GsonFactory.newNamingGson();
        else
            gson = GsonFactory.newNormalGson();
        arg = gson.toJson(o);
        if (encode) {
            arg = arg.replace("\"", "\\\"");
        } else {
            if (arg.startsWith("\""))
                arg = arg.substring(1);
            if (arg.endsWith("\""))
                arg = arg.substring(0, arg.length() - 1);
        }

        String result = String.format("javascript:%s(" + arg + ")", stringArgs);
        LogUtil.d(TAG, "load js : " + result);
        return result;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        return false;
    }

    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    public void goBack() {
        mWebView.goBack();
    }

    private boolean handleUrl(UrlModel urlModel) {
        if (TextUtils.equals(urlModel.type, FUNC_CONFIG)) {
            webConfig(urlModel.args);
        } else if (TextUtils.equals(urlModel.type, FUNC_GET_ACCOUNT)) {
            webGetAccount(urlModel.call);
        } else if (TextUtils.equals(urlModel.type, FUNC_GET_ACCESS_TOKEN)) {
            webGetAccessToken(urlModel.args, urlModel.call);
        } else if (TextUtils.equals(urlModel.type, FUNC_LOG)) {
            webLog(urlModel.args);
        } else if (TextUtils.equals(urlModel.type, FUNC_GET_OTHERS)) {
            webGetOthers(urlModel.call);
        } else {
            LogUtil.i(TAG, "unknown type");
        }
        return true;
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
    private boolean parseHandleUrl(Context context, String url, int posX, int posY) {
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
            return handleUrl(urlModel);
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
            QuestionFragment_.builder().type(Procedure.QUESTION_TYPE_INFO).build();
        } else if (TextUtils.equals(des, "video_question_list")) {

        }
        return true;
    }

    public interface WebViewCallable {
        void call(WebView webView);
    }

    @SuppressWarnings("unused")
    public interface WebViewLoadListener {
        void loadOver();
    }

    private class BaseWebClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            loadOver();
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            url = urlDecode(url);
            LogUtil.d(TAG, url);
            return parseHandleUrl(getContext(), url, (int) (touchX + posX), (int) (touchY + posY))
                    || super.shouldOverrideUrlLoading(view, url);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            LogUtil.d(TAG, description + "  " + failingUrl);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @TargetApi(23)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            LogUtil.d(TAG, "error : " + request.getUrl() + error.toString());
            super.onReceivedError(view, request, error);
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            LogUtil.d(TAG, event.getKeyCode() + " " + event.getAction());
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                }
                return true;
            }
            return super.shouldOverrideKeyEvent(view, event);
        }
    }

    class UrlModel {
        String type;
        String call;
        Map<String, String> args;
    }
}
