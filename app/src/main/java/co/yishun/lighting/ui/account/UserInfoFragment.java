package co.yishun.lighting.ui.account;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.Calendar;

import co.yishun.lighting.Constants;
import co.yishun.lighting.R;
import co.yishun.lighting.account.AccountManager;
import co.yishun.lighting.api.APIFactory;
import co.yishun.lighting.api.Account;
import co.yishun.lighting.api.model.Token;
import co.yishun.lighting.api.model.User;
import co.yishun.lighting.ui.common.BaseFragment;
import co.yishun.lighting.ui.view.LocationChooseDialog;
import co.yishun.lighting.util.LogUtil;
import co.yishun.lighting.util.Util;
import retrofit2.Response;

/**
 * Created by carlos on 4/6/16.
 */
@EFragment(R.layout.fragment_user_info)
public class UserInfoFragment extends BaseFragment
        implements AccountManager.OnUserInfoChangeListener,
        AccountActivity.PictureCroppedHandler {

    public static final String TAG = "UserInfoFragment";
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

    @FragmentArg
    Token token;
    @FragmentArg
    String phone;
    private Uri croppedProfileUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AccountManager.addOnUserInfoChangedListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

        genderFragment.setOnClickListener(this::genderClicked);
        sexualityFragment.setOnClickListener(this::genderClicked);

        birthDayFragment.setOnClickListener(this::birthDayClicked);
        locationFragment.setOnClickListener(this::locationClicked);

        invalidateUserInfo(AccountManager.getUserInfo(getContext()));
    }

    void usernameClicked(View view) {
        Context context = view.getContext();
        new MaterialDialog.Builder(context).theme(Theme.DARK)
                .title(getString(R.string.activity_user_info_username))
                .input(getString(R.string.activity_user_info_username),
                        AccountManager.getUserInfo(context).nickname, false,
                        (MaterialDialog dialog, CharSequence input) -> {
                            if (TextUtils.equals(input, AccountManager.getUserInfo(context).nickname))
                                return;
                            updateUserInfo(AccountManager.getUserInfo(context).id,
                                    input.toString(), null, null, null);
                        })
                .build().show();
    }

    void genderClicked(View view) {
        Context context = view.getContext();
        Account.Gender mSelectGender = AccountManager.getUserInfo(context).getGender();
        new MaterialDialog.Builder(context)
                .theme(Theme.LIGHT)
                .title(R.string.view_gender_spinner_title)
                .items(R.array.view_gender_spinner_items)
                .itemsCallbackSingleChoice(mSelectGender.toInt() % 2, (dialog, view1, which, text) -> {
                    Account.Gender gender = Account.Gender.format(which);
                    if (gender == AccountManager.getUserInfo(context).getGender())
                        return true;
                    updateUserInfo(AccountManager.getUserInfo(context).id, null, gender, null, null);
                    return true; // allow selection
                })
                .positiveText(R.string.view_gender_spinner_positive_btn)
                .show();
    }

    void birthDayClicked(View view) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(view.getContext(), (view1, year, monthOfYear, dayOfMonth) -> {

        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    void locationClicked(View view) {
        Context context = view.getContext();
        new LocationChooseDialog.Builder(context)
                .build()
                .setLocationSelectedListener((String location, Pair<String, String> provinceAndDistrict) -> {
                    if (TextUtils.equals(location, AccountManager.getUserInfo(context).location))
                        return;
                    updateUserInfo(AccountManager.getUserInfo(context).id, null, null, null, location);
                })
                .show();
    }

    @Override
    public void onPictureSelectedFailed(Exception e) {

    }

    @Override
    public void onPictureCropped(Uri uri) {
        croppedProfileUri = uri;
        updateAvatar(AccountManager.getUserInfo(getContext()).id);
//        Picasso.with(this).load(uri).memoryPolicy(MemoryPolicy.NO_STORE).memoryPolicy(MemoryPolicy.NO_CACHE).into(avatarImage);
    }

    @Background
    void updateAvatar(@NonNull String userId) {
        if (croppedProfileUri == null) return;
        String uriString = croppedProfileUri.toString();
        String path = uriString.substring(uriString.indexOf(":") + 1);
        String qiNiuKey = Constants.PROFILE_PREFIX + userId + Constants.URL_HYPHEN + Util.unixTimeStamp() + Constants.PROFILE_SUFFIX;

        //TODO add new update user info code
        updateUserInfo(userId, null, null, qiNiuKey, null);
    }

    @Background
    void updateUserInfo(String userId, String nickname, Account.Gender gender, String qiNiuKey, String location) {
        User user = new User();
        try {
            Response<Void> response = APIFactory.getAccountAPI().changePersonalInfo(token.userId, token.accessToken,
                    user).execute();
            if (response.isSuccessful()) {

                AccountManager.updateOrCreateUserInfo(getContext(), user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void invalidateUserInfo(User user) {
        if (user == null) {
            return;
        }
        LogUtil.i(TAG, "user info: " + user);
        Picasso.with(getContext()).load(user.avatar).into(avatarImage);

        nicknameFragment.setContent(user.nickname);
        String weiboID;
        if (TextUtils.isEmpty(user.weiboUid)) {
            weiboID = getString(R.string.activity_user_info_weibo_id_unbound);
        } else {
            weiboID = user.weiboNickname;
        }
//        weiboFragment.setContent(weiboID);
        String gender;
        Account.Gender orin = user.getGender();
        if (orin != null)
            switch (user.getGender()) {
                case FEMALE:
                    gender = "♀";
                    break;
                case MALE:
                    gender = "♂";
                    break;
                default:
                    gender = getString(R.string.activity_user_info_gender_unknown);
                    break;
            }
        else gender = getString(R.string.activity_user_info_gender_unknown);
        genderFragment.setContent(gender);
        locationFragment.setContent(user.location);
    }

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @Override
    public void onUserInfoChange(User info) {
        invalidateUserInfo(info);
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
