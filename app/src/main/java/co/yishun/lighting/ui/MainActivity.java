package co.yishun.lighting.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.yishun.lighting.R;
import co.yishun.lighting.ui.view.ResideLayout;
import co.yishun.lighting.ui.view.SettingsActivity;
import co.yishun.lighting.util.FileUtil;
import co.yishun.lighting.web.LUWebViewClient;


public class MainActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.reside_layout)
    ResideLayout resideLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resideLayout.openPane();
            }
        });
        setWebView();
    }

    @Override
    public String getPageInfo() {
        return "MainActivity";
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebView() {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            //noinspection deprecation
            webView.getSettings().setDatabasePath(FileUtil.getDatabasePath(this));
        }

        webView.setWebViewClient(new LUWebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }

        webView.loadUrl("http://www.yishun.co/");
    }

    public void onNavigationItemClicked(View view) {
        switch (view.getId()) {
            case R.id.navigation_item_lightup:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.navigation_item_mssage:
                ShootActivity_.intent(this).start();
                break;
            case R.id.navigation_item_profile:
                UserInfoActivity_.intent(this).start();
                break;
            case R.id.navigation_item_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
    }
}
