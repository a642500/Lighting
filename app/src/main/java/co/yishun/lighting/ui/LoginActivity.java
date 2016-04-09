package co.yishun.lighting.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

import co.yishun.lighting.R;
import co.yishun.lighting.account.AccountManager;
import co.yishun.lighting.api.APIFactory;
import co.yishun.lighting.api.model.User;
import co.yishun.lighting.ui.account.AccountActivity_;
import co.yishun.lighting.ui.common.BaseActivity;
import co.yishun.lighting.ui.view.PageIndicatorDot;
import retrofit2.Call;
import retrofit2.Response;

import static co.yishun.lighting.ui.UIStatus.STATUS_NETWORKING;
import static co.yishun.lighting.ui.UIStatus.STATUS_NOTHING;

/**
 * A login screen that offers login via email/password.
 */
@Fullscreen
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    private static final java.lang.String TAG = "LoginActivity";
    @ViewById
    ViewPager viewPager;
    @ViewById
    Button signInBtn;
    @ViewById
    EditText phoneEditText;
    @ViewById
    EditText passwordEditText;
    @ViewById
    ImageView backgroundImageViewA;
    @ViewById
    ImageView backgroundImageViewB;
    @ViewById
    ImageView backgroundImageViewC;
    @ViewById
    PageIndicatorDot pageIndicatorDot;
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    @UIStatus
    private int status = STATUS_NOTHING;

    public static boolean isPhoneValid(@NonNull String phone) {
        //TODO: Replace this with your own logic
        try {
            //noinspection ResultOfMethodCallIgnored
            Long.parseLong(phone);
            return phone.trim().length() == 11;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public static boolean isPasswordValid(@NonNull String password) {
        return password.length() >= 6 && password.length() <= 30;
    }

    @EditorAction(R.id.passwordEditText)
    boolean passwordEditTextEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
            attemptLogin();
            return true;
        }
        return false;
    }

    @AfterViews
    void setUpViewPager() {
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new PagerAdapter() {
            @DrawableRes
            int[] mRes = new int[]{
                    R.drawable.pic_login_1,
                    R.drawable.pic_login_2,
                    R.drawable.pic_login_3
            };

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(LoginActivity.this);
                imageView.setImageResource(mRes[position]);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                container.addView(imageView, new ViewPager.LayoutParams());
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(((View) object));
            }
        });
        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        pageIndicatorDot.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        backgroundImageViewA.setAlpha(1 - positionOffset);
                        backgroundImageViewB.setAlpha(positionOffset);
                        break;
                    case 1:
                        backgroundImageViewB.setAlpha(1 - positionOffset);
                        backgroundImageViewC.setAlpha(positionOffset);
                        break;
                }

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Click(R.id.signInBtn)
    void attemptLogin() {
        if (status > 0) {
            return;
        }

        // Reset errors.
        phoneEditText.setError(null);
        passwordEditText.setError(null);

        // Store values at the time of the login attempt.
        String phone = phoneEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.activity_login_error_incorrect_password));
            focusView = passwordEditText;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.activity_login_error_wrong_password_length));
            focusView = passwordEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            phoneEditText.setError(getString(R.string.activity_login_error_empty_phone));
            focusView = phoneEditText;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            phoneEditText.setError(getString(R.string.activity_login_error_invalid_phone));
            focusView = phoneEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            status = STATUS_NETWORKING;
            login(phone, password);
        }
    }

    @Click
    void signUpBtnClicked() {
        AccountActivity_.intent(this).phone(phoneEditText.getText().toString()).start();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

//        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            }
//        });

    }

    @Background(id = CANCEL_WHEN_DESTROY)
    void login(String phone, String password) {
        safelyDoWithActivity(activity -> {
            Call<User> call = APIFactory.getAccountAPI().login(phone, "password", password, null);
            Response<User> userResponse = call.execute();
            if (userResponse.isSuccessful()) {
                User user = userResponse.body();
                showSnackMsg(R.string.activity_login_msg_success);

                AccountManager.saveAccount(activity, user);
                IntegrateInfoActivity_.intent(this).start();
//                MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
// Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
            } else {
                passwordEditText.setError(getString(R.string.activity_login_error_incorrect_password));
                passwordEditText.requestFocus();
            }
        });
        status = STATUS_NOTHING;
    }

    @Override
    public String getPageInfo() {
        return TAG;
    }
}

