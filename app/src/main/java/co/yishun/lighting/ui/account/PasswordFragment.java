package co.yishun.lighting.ui.account;

import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import co.yishun.lighting.R;
import co.yishun.lighting.api.model.Token;
import co.yishun.lighting.ui.LoginActivity;
import co.yishun.lighting.ui.common.BaseFragment;

/**
 * Created by carlos on 4/7/16.
 */
@EFragment(R.layout.fragment_password)
public class PasswordFragment extends BaseFragment {
    private static final String TAG = "PasswordFragment";
    @FragmentArg
    Token token;
    @ViewById
    Toolbar toolbar;
    @ViewById
    View nextBtn;

    @Override

    public String getPageInfo() {
        return TAG;
    }

    @Click
    void nextBtnClicked() {
        ((AccountActivity) getActivity()).goToUserInfo(token);
    }

    @AfterTextChange(R.id.passwordEditText)
    void onPasswordEditTextChange(Editable text) {
        String password = text.toString();
        nextBtn.setEnabled(password != null && LoginActivity.isPhoneValid(password));
    }

    @AfterViews
    void setViews() {
        toolbar.setNavigationOnClickListener(v -> NavUtils.navigateUpFromSameTask(getActivity()));
    }
}
