package co.yishun.lighting.ui;

import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

import co.yishun.lighting.R;
import co.yishun.lighting.api.APIFactory;
import co.yishun.lighting.ui.view.CountDownResentView;
import retrofit2.Response;

/**
 * Created by carlos on 3/30/16.
 */
@EFragment(R.layout.fragment_sign_up)
public class SignUpFragment extends BaseFragment {
    public static final String TAG = "SignUpFragment";
    @FragmentArg
    String phone;
    @ViewById
    TextInputEditText phoneEditText;
    @ViewById
    Toolbar toolbar;
    @ViewById
    CountDownResentView resentView;

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @AfterViews
    void autoSendSms() {
        if (!TextUtils.isEmpty(phone)) {
            phoneEditText.setText(phone);
            phoneEditText.setSelection(phoneEditText.length());
        }
        trySendSms();
    }

    @AfterViews
    void setViews() {
        toolbar.setNavigationOnClickListener(v -> NavUtils.navigateUpFromSameTask(getActivity()));

        resentView.setOnClickListenerWhenEnd(view -> {
            phone = phoneEditText.getText().toString();
            trySendSms();
        });

    }

    private void trySendSms() {
        if (LoginActivity.isPhoneValid(phone)) {
            resentView.countDown();
            sendSms(phone);
        }
    }

    @Background
    void sendSms(final String phone) {
        try {
            Response response = APIFactory.getAccountAPI().register(phone).execute();
            if (response.isSuccessful()) {
                ((BaseActivity) getActivity()).showSnackMsg(R.string.fragment_sign_up_msg_send_ok);
            } else {
                ((BaseActivity) getActivity()).showSnackMsg(R.string.fragment_sign_up_msg_send_fail);
                onFail();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void onFail() {
        resentView.onEnd();
    }


}
