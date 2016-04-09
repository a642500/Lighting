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

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.Map;

import co.yishun.lighting.Constants;
import co.yishun.lighting.account.AccountManager;
import co.yishun.lighting.util.FileUtil;
import co.yishun.lighting.util.GsonFactory;
import co.yishun.lighting.util.LogUtil;


/**
 * Created by Jinge on 2016/1/21.
 */
@EFragment
public abstract class BaseWebFragment extends BaseFragment {
    public static final String TAG_WEB = "web";
    private static final String TAG = "BaseWebFragment";
    private static boolean needGlobalRefresh = false;
    @ViewById
    protected SwipeRefreshLayout swipeRefreshLayout;
    @ViewById
    protected WebView webView;
    protected BaseActivity mActivity;
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
    private Context mApplicationContext;

    public static void invalidateWeb() {
        needGlobalRefresh = true;
    }

    @Override
    public Context getContext() {
        return super.getContext() == null ? mApplicationContext : super.getContext();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) getActivity();
        if (mApplicationContext == null) {
            mApplicationContext = mActivity.getApplicationContext();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.clearCache(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRefreshable && needGlobalRefresh) {
            needGlobalRefresh = false;
            reload();
        }
    }

    @AfterInject
    void setDefault() {
        mHybridDir = FileUtil.getInternalFile(getContext(), Constants.HYBRD_UNZIP_DIR);
        if (TextUtils.isEmpty(mUrl)) {
            mUrl = Constants.FILE_URL_PREFIX +
                    new File(mHybridDir, "build/pages/world/world.html").getPath();
            LogUtil.i(TAG, "url is null, load default");
        }

        int lastUpdateTime = getContext().getSharedPreferences(Constants.Perference.RUNTIME_PREFERENCE,
                Context.MODE_PRIVATE).getInt(Constants.Perference.PREFERENCE_HYBRID_UPDATE_TIME, 0);
        if (mUrl.startsWith(Constants.FILE_URL_PREFIX)) mUrl += "?time=" + lastUpdateTime;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @CallSuper
    protected void setUpWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setDatabasePath(FileUtil.getDatabasePath(getContext()));
        }

        webView.setWebViewClient(new BaseWebClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getContext().getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
        webView.setOnTouchListener((v, event) -> {
            touchX = event.getX();
            touchY = event.getY();
            return false;
        });
        webView.post(() -> {
            int location[] = new int[2];
            webView.getLocationOnScreen(location);
            posX = location[0];
            posY = location[1];
        });
        webView.loadUrl(mUrl);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (mRefreshable) reload();
        });
    }

    public void setRefreshable(boolean refreshable) {
        mRefreshable = refreshable;
    }

    public void reload() {
        if (webView != null)
            webView.reload();
    }

    private void loadOver() {
        swipeRefreshLayout.setRefreshing(false);
    }

//    private void webGetEnv(Map<String,String args) {
//        @SuppressWarnings("ConstantConditions") String env = Constants.SANDBOX ? "development" : "production";
//        webView.loadUrl(String.format(toJs(env), HybridUrlHandler.FUNC_GET_ENV));
//    }

    private void webGetAccount(String call) {
        webView.loadUrl(String.format(
                toJs(AccountManager.getUserInfo(getContext()), true, true),
                call));
    }
//
//    private void webGetAccountId(Map<String,String args) {
//        webView.loadUrl(String.format(toJs(AccountManager.getUserInfo(getContext()).id),
//                HybridUrlHandler.FUNC_GET_ACCOUNT_ID));
//    }

    private void webLog(Map<String, String> args) {
        LogUtil.i(TAG, "js log : " + args.get("text"));
    }

    private void webConfig(Map<String, String> args) {
        boolean h = Boolean.valueOf(args.get("h_scroll_indicator"));
        webView.setHorizontalScrollBarEnabled(h);
        boolean v = Boolean.valueOf(args.get("v_scroll_indicator"));
        webView.setVerticalScrollBarEnabled(v);
    }

    private void webGetAccessToken(Map<String, String> args, String call) {
        String id = args.get("user_id");
        //TODO
        safelyDoWithToken((token) ->
                webView.loadUrl(String.format(toJs(token, true, true), call)));
    }

    private void webGetOthers(String call) {
        //TODO
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
//        webView.loadUrl(String.format(toJs(mArg, false), HybridUrlHandler.FUNC_LOAD));
//    }

//    private void webAuth(Map<String,String args) {
//        webView.loadUrl(String.format(toJs(OneMomentClientV4.getAuthStr()),
//                HybridUrlHandler.FUNC_GET_BASIC_AUTH_HEADER));
//    }

    public void sendFinish() {
        String result = "javascript:ctx.finish()";
        LogUtil.d(TAG, "load js : " + result);
        webView.loadUrl(result);
    }

    public String toJs(Object o) {
        return toJs(o, true);
    }

    public String toJs(Object o, boolean encode, boolean naming) {
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

        String result = "javascript:%sReturn(" + arg + ")";
        LogUtil.d(TAG, "load js : " + result);
        return result;
    }

    public String toJs(Object o, boolean encode) {
        return toJs(o, encode, false);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return false;
    }

    public boolean canGoBack() {
        return webView.canGoBack();
    }

    public void goBack() {
        webView.goBack();
    }

    public File getHybrdDir() {
        return mHybridDir;
    }

    public interface WebViewLoadListener {
        void loadOver();
    }

    private class BaseWebClient extends WebViewClient {

        private HybridUrlHandler urlHandler = new HybridUrlHandler() {
            @Override
            protected boolean handleInnerUrl(UrlModel urlModel) {
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
        };

        @Override
        public void onPageFinished(WebView view, String url) {
            loadOver();
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            url = HybridUrlHandler.urlDecode(url);
            LogUtil.d(TAG, url);
            return urlHandler.handleUrl(getContext(), url, (int) (touchX + posX), (int) (touchY + posY))
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
                if (webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return true;
            }
            return super.shouldOverrideKeyEvent(view, event);
        }
    }
}
