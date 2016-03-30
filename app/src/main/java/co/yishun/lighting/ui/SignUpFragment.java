package co.yishun.lighting.ui;

import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by carlos on 3/30/16.
 */
@EFragment
public class SignUpFragment extends BaseFragment {
    public static final String TAG = "SignUpFragment";
    @Extra
    String phone;
    @ViewById
    TextInputEditText phoneEditText;

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @AfterViews
    void autoSendSms() {
        if (!TextUtils.isEmpty(phone)) {
            phoneEditText.setText(phone);
        }

        if (LoginActivity.isPhoneValid(phone)) {
            sendSms(phone);
        }
    }

    private void sendSms(String phone) {

    }


}
