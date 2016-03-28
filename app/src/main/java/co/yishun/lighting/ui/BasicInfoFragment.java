package co.yishun.lighting.ui;

import org.androidannotations.annotations.EFragment;

import co.yishun.lighting.R;

/**
 * Created by carlos on 3/28/16.
 */
@EFragment(R.layout.fragment_basic_info)
public class BasicInfoFragment extends BaseFragment {
    public static final String TAG = "BasicInfoFragment";

    @Override
    public String getPageInfo() {
        return TAG;
    }


}
