package co.yishun.lighting.web;

import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import co.yishun.lighting.util.LogUtil;

/**
 * Created by Carlos on 2016/3/22.
 */
public class LUWebViewClient extends WebViewClient {
    private static final String TAG = "LUWebViewClient";

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        LogUtil.d(TAG, event.getKeyCode() + " " + event.getAction());
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (view.canGoBack()) {
                view.goBack();
                return true;
            }
            return true;
        }
        return super.shouldOverrideKeyEvent(view, event);
    }

}
