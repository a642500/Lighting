package co.yishun.lighting.ui;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import co.yishun.lighting.api.model.Token;

/**
 * Created by carlos on 4/6/16.
 */
@EFragment
public class UserInfoFragment extends BaseFragment {

    public static final String TAG = "UserInfoFragment";

    @FragmentArg
    Token token;

    @Override
    public String getPageInfo() {
        return TAG;
    }
}
