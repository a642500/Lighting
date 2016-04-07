package co.yishun.lighting.ui.account;

import android.support.annotation.IntDef;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

import co.yishun.lighting.R;
import co.yishun.lighting.api.APIFactory;
import co.yishun.lighting.api.model.Token;
import co.yishun.lighting.ui.LoginActivity;
import co.yishun.lighting.ui.common.BaseFragment;
import retrofit2.Response;

/**
 * Created by carlos on 4/7/16.
 */
@EFragment(R.layout.fragment_password)
public class PasswordFragment extends BaseFragment {
    public static final int STATUS_NETWORKING = 1;
    public static final int STATUS_NOTHING = 0;
    private static final String TAG = "PasswordFragment";
    @FragmentArg
    Token token;
    @ViewById
    Toolbar toolbar;
    @ViewById
    TextInputEditText passwordEditText;
    @ViewById
    View nextBtn;
    @FragmentArg
    String phone;
    @Status
    private int status = STATUS_NOTHING;

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @Click
    void nextBtnClicked() {
        if (status > STATUS_NOTHING) {
            return;
        }
        passwordEditText.setError(null);

        final String password = passwordEditText.getText().toString();

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.activity_login_error_incorrect_password));
        } else if (!LoginActivity.isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.activity_login_error_wrong_password_length));
        } else {
            status = STATUS_NETWORKING;
            changePassword(password);
        }
    }

    @Background
    void changePassword(final String password) {
        final AccountActivity accountActivity = (AccountActivity) getActivity();
        try {
            Response<Void> response = APIFactory.getAccountAPI().changePassword(phone, password).execute();
            if (response.isSuccessful()) {
                accountActivity.showSnackMsg(R.string.fragment_password_msg_ok);
                accountActivity.goToUserInfo(phone, token);
            } else {
                accountActivity.showSnackMsg(R.string.fragment_password_msg_fail);
            }
        } catch (IOException e) {
            accountActivity.showSnackMsg(R.string.fragment_password_error_network);
        }
        status = STATUS_NOTHING;
    }

    @AfterViews
    void setViews() {
        toolbar.setNavigationOnClickListener(v -> NavUtils.navigateUpFromSameTask(getActivity()));
    }

    @IntDef({STATUS_NOTHING, STATUS_NETWORKING})
    private @interface Status {
    }
}
