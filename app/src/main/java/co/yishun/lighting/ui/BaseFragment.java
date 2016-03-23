package co.yishun.lighting.ui;

import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by yyz on 8/3/15.
 */
public abstract class BaseFragment extends Fragment {
    //set it true and give a page name in setPageInfo(), if we take this fragment into count.
    protected boolean mIsPage = true;
    protected String mPageName = "BaseFragment";

    public abstract String getPageInfo();

    @Override
    public void onResume() {
        super.onResume();
        if (mIsPage) {
            MobclickAgent.onPageStart(getPageInfo());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mIsPage) {
            MobclickAgent.onPageEnd(getPageInfo());
        }
    }
}
