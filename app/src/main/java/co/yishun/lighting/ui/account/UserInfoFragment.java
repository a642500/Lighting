package co.yishun.lighting.ui.account;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import co.yishun.lighting.Constants;
import co.yishun.lighting.R;
import co.yishun.lighting.account.AccountManager;
import co.yishun.lighting.api.APIFactory;
import co.yishun.lighting.api.Account;
import co.yishun.lighting.api.model.Token;
import co.yishun.lighting.api.model.User;
import co.yishun.lighting.ui.MainActivity_;
import co.yishun.lighting.ui.SplashActivity_;
import co.yishun.lighting.ui.common.BaseActivity;
import co.yishun.lighting.ui.common.BaseFragment;
import co.yishun.lighting.ui.view.LocationChooseDialog;
import co.yishun.lighting.util.LogUtil;
import co.yishun.lighting.util.Util;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by carlos on 4/6/16.
 */
@EFragment(R.layout.fragment_user_info)
public class UserInfoFragment extends BaseFragment
        implements AccountManager.OnUserInfoChangeListener,
        AccountActivity.PictureCroppedHandler {

    public static final String TAG = "UserInfoFragment";
    public static final int EDIT_MODE_COMMIT_LAST = 0;
    public static final int EDIT_MODE_COMMIT_EVERY_TIME = 1;
    public static final int EDIT_MODE_COMMIT_EVERY_TIME_WITH_SIGN_OUT = 2;
    @ViewById
    Toolbar toolbar;
    @ViewById
    RelativeLayout avatarLayout;
    @ViewById
    ImageView avatarImage;
    @FragmentById(childFragment = true)
    ItemFragment nicknameFragment;
    @FragmentById(childFragment = true)
    ItemFragment wechatFragment;
    @FragmentById(childFragment = true)
    ItemFragment genderFragment;
    @FragmentById(childFragment = true)
    ItemFragment sexualityFragment;
    @FragmentById(childFragment = true)
    ItemFragment birthDayFragment;
    @FragmentById(childFragment = true)
    ItemFragment locationFragment;
    @ViewById
    View finishBtn;
    @ViewById
    View signOutBtn;

    @FragmentArg
    Token token;
    @FragmentArg
    String phone;
    @EditMode
    @FragmentArg
    int editMode = EDIT_MODE_COMMIT_LAST;

    User lastFillUser;
    private Uri croppedProfileUri;

    public static SimpleDateFormat getTimeFormat() {
        return new SimpleDateFormat(Constants.TIME_FORMAT, Locale.CHINA);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (editMode == EDIT_MODE_COMMIT_EVERY_TIME)
            AccountManager.addOnUserInfoChangedListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (editMode == EDIT_MODE_COMMIT_EVERY_TIME)
            AccountManager.removeOnUserInfoChangedListener(this);
    }

    @AfterViews
    void setupViews() {
        toolbar.setNavigationOnClickListener(v -> NavUtils.navigateUpFromSameTask(getActivity()));

        avatarLayout.setOnClickListener(v -> Crop.pickImage(getActivity()));

        nicknameFragment.setTitle(getString(R.string.activity_user_info_username));
        wechatFragment.setTitle(getString(R.string.activity_user_info_wechat));

        genderFragment.setTitle(getString(R.string.activity_user_info_gender));
        sexualityFragment.setTitle(getString(R.string.fragment_user_info_sexuality));

        birthDayFragment.setTitle(getString(R.string.fragment_user_info_birthday));
        locationFragment.setTitle(getString(R.string.activity_user_info_location));

        nicknameFragment.setOnClickListener(this::usernameClicked);
        wechatFragment.setOnClickListener(this::weChatClicked);

        genderFragment.setOnClickListener(this::genderClicked);
        sexualityFragment.setOnClickListener(this::sexualityClicked);

        birthDayFragment.setOnClickListener(this::birthDayClicked);
        locationFragment.setOnClickListener(this::locationClicked);

        finishBtn.setVisibility(editMode == EDIT_MODE_COMMIT_LAST ? View.VISIBLE : View.INVISIBLE);
        signOutBtn.setVisibility(editMode == EDIT_MODE_COMMIT_EVERY_TIME_WITH_SIGN_OUT ?
                View.VISIBLE : View.INVISIBLE);

        invalidateUserInfo(null);
    }

    void usernameClicked(View view) {
        Context context = view.getContext();
        new MaterialDialog.Builder(context).theme(Theme.DARK)
                .title(getString(R.string.activity_user_info_username))
                .input(getString(R.string.activity_user_info_username),
                        lastFillUser.nickname, false,
                        (MaterialDialog dialog, CharSequence input) -> {
                            if (TextUtils.equals(input, lastFillUser.nickname))
                                return;
                            User newUser = new User();
                            newUser.nickname = input.toString();
                            commit(newUser);
                        })
                .build().show();
    }

    void weChatClicked(View view) {
        //TODO add wechat
    }

    void genderClicked(View view) {
        final Context context = view.getContext();
        final Account.Gender mSelectGender = lastFillUser.getSex();
        new MaterialDialog.Builder(context)
                .theme(Theme.DARK)
                .title(R.string.view_gender_spinner_title)
                .items(R.array.view_gender_spinner_items)
                .itemsCallbackSingleChoice(mSelectGender.toInt() % 2, (dialog, view1, which, text) -> {
                    Account.Gender gender = Account.Gender.format(which);
                    if (gender == mSelectGender)
                        return true;
                    User newUser = new User();
                    newUser.setSex(gender);
                    commit(newUser);
                    return true; // allow selection
                })
                .positiveText(R.string.view_gender_spinner_positive_btn)
                .show();
    }

    void sexualityClicked(View view) {
        final Context context = view.getContext();
        final Account.Gender mSelectGender = lastFillUser.getSexuality();
        new MaterialDialog.Builder(context)
                .theme(Theme.DARK)
                .title(R.string.view_gender_spinner_title)
                .items(R.array.view_gender_spinner_items)
                .itemsCallbackSingleChoice(mSelectGender.toInt() % 2, (dialog, view1, which, text) -> {
                    Account.Gender gender = Account.Gender.format(which);
                    if (gender == mSelectGender)
                        return true;
                    User newUser = new User();
                    newUser.setSexuality(gender);
                    commit(newUser);
                    return true; // allow selection
                })
                .positiveText(R.string.view_gender_spinner_positive_btn)
                .show();
    }

    void birthDayClicked(View view) {
        Date birthday;
        try {
            birthday = new Date(Long.parseLong(lastFillUser.birthday) * 1000);
        } catch (NumberFormatException | NullPointerException e) {
            birthday = new Date();
        }

        final Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(birthday);
        int oYear = calendar.get(Calendar.YEAR) - 18;
        int oMonth = calendar.get(Calendar.MONTH);
        int oDay = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(view.getContext(), (view1, year, monthOfYear, dayOfMonth) -> {
            if (year != oYear || monthOfYear != oMonth || dayOfMonth != oDay) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                User newUser = new User();
                newUser.birthday = String.valueOf(calendar.getTime().getTime() / 1000);
                commit(newUser);
            }
        }, oYear, oMonth, oDay).show();
    }

    void locationClicked(View view) {
        Context context = view.getContext();
        //TODO select origin location
        new LocationChooseDialog.Builder(context).build()
                .setLocationSelectedListener((String location, Pair<String, String> provinceAndDistrict) -> {
                    if (TextUtils.equals(location, lastFillUser.location))
                        return;
                    User newUser = new User();
                    newUser.location = location;
                    commit(newUser);
                }).show();
    }

    private void commit(final User user) {
        switch (editMode) {
            case EDIT_MODE_COMMIT_LAST:
                invalidateUserInfo(user);
                break;
            case EDIT_MODE_COMMIT_EVERY_TIME:
                updateUserInfo(user, false);
                break;
        }
    }

    @Click
    void finishBtnClicked(View view) {

        if (TextUtils.isEmpty(lastFillUser.nickname)) {
            //TODO check
        }
        updateUserInfo(lastFillUser, true);
    }

    @Click
    void signOutBtnClicked(View view) {
        AccountManager.deleteAccount(view.getContext());
        Toast.makeText(view.getContext(), R.string.fragment_user_info_msg_sign_out
                , Toast.LENGTH_SHORT).show();
        SplashActivity_.intent(this).action(Intent.ACTION_MAIN)
                .flags(Intent.FLAG_ACTIVITY_CLEAR_TASK).start();
    }

    @Override
    public void onPictureSelectedFailed(Exception e) {

    }

    @Override
    public void onPictureCropped(Uri uri) {
        croppedProfileUri = uri;
        //TODO commit
//        updateAvatar(AccountManager.getUserInfo(getContext()).id);
//        Picasso.with(this).load(uri).memoryPolicy(MemoryPolicy.NO_STORE).memoryPolicy(MemoryPolicy.NO_CACHE).into(avatarImage);
    }

    @Background(id = CANCEL_WHEN_DESTROY)
    void updateAvatar(@NonNull String userId) {
        if (croppedProfileUri == null) return;
        String uriString = croppedProfileUri.toString();
        String path = uriString.substring(uriString.indexOf(":") + 1);
        String qiNiuKey = Constants.PROFILE_PREFIX + userId + Constants.URL_HYPHEN + Util.unixTimeStamp() + Constants.PROFILE_SUFFIX;

        //TODO add new update user info code
//        updateUserInfo(userId, null, null, qiNiuKey, null);
    }

    @Background(id = CANCEL_WHEN_DESTROY)
    void updateUserInfo(final User user, boolean exitWhenSuccess) {
        safelyDoWithContext((context) -> {
            Call<Void> call = APIFactory.getAccountAPI().changePersonalInfo(token.userId, token.accessToken,
                    user);
            Response<Void> response = call.execute();
            if (response.isSuccessful()) {
                AccountManager.saveUserToken(context, user.accessToken);
                if (exitWhenSuccess)
                    AccountManager.saveAccount(context, user);// for sign up
                else
                    AccountManager.updateOrCreateUserInfo(context, user);// for update user info
                if (exitWhenSuccess) {
                    MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                }
            } else {
                showSnackMsg(R.string.fragment_user_info_msg_update_info_fail);
            }
        });
    }

    @Nullable
    private BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @UiThread
    void invalidateUserInfo(@Nullable final User changed) {
        if (lastFillUser == null) {
            lastFillUser = User.dummyUser();
        }
        if (changed == null) {
            lastFillUser.add(AccountManager.getUserInfo(getContext()));
        } else {
            lastFillUser.add(changed);
        }

        LogUtil.i(TAG, "user changed: " + changed);
        LogUtil.i(TAG, "user info: " + lastFillUser);

        Picasso.with(getContext()).load(lastFillUser.portrait).into(avatarImage);

        nicknameFragment.setContent(lastFillUser.nickname);
        String weChatName;
        if (TextUtils.isEmpty(lastFillUser.wechatUid)) {
            weChatName = getString(R.string.activity_user_info_weibo_id_unbound);
        } else {
            weChatName = lastFillUser.wechatNickname;
        }
        wechatFragment.setContent(weChatName);

        String gender = getString(R.string.activity_user_info_gender_unknown);
        Account.Gender orin = lastFillUser.getSex();
        if (orin != null) {
            gender = orin.getDisplayText(getContext());
        }
        genderFragment.setContent(gender);

        String sexuality = getString(R.string.activity_user_info_gender_unknown);
        Account.Gender sexualityOrigin = lastFillUser.getSexuality();
        if (sexualityOrigin != null) {
            sexuality = sexualityOrigin.getDisplayText(getContext());
        }
        sexualityFragment.setContent(sexuality);


        String birthday;
        try {
            birthday = getTimeFormat().format(new Date(Long.parseLong(lastFillUser.birthday)));
        } catch (NumberFormatException e) {
            birthday = null;
        }
        if (TextUtils.isEmpty(lastFillUser.birthday)) {
            birthday = getString(R.string.fragment_user_info_birthday_default);
        }

        birthDayFragment.setContent(birthday);

        locationFragment.setContent(lastFillUser.location);
    }

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @Override
    public void onUserInfoChange(User info) {
        invalidateUserInfo(info);
    }

    @IntDef({EDIT_MODE_COMMIT_LAST,
            EDIT_MODE_COMMIT_EVERY_TIME,
            EDIT_MODE_COMMIT_EVERY_TIME_WITH_SIGN_OUT})
    public @interface EditMode {
    }

    public static class ItemFragment extends BaseFragment {
        ViewGroup rootView;
        TextView itemTitle;
        TextView itemContent;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user_info_item, container, false);
            itemTitle = (TextView) rootView.findViewById(R.id.itemTitle);
            itemContent = (TextView) rootView.findViewById(R.id.itemContent);
            return rootView;
        }

        void setTitle(String title) {
            itemTitle.setText(title);
        }

        void setContent(String content) {
            itemContent.setText(content);
        }

        void setOnClickListener(View.OnClickListener listener) {
            rootView.setOnClickListener(listener);
        }

        @Override
        public String getPageInfo() {
            mIsPage = false;
            return "ItemFragment";
        }
    }
}
