package co.yishun.lighting.ui.account;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import co.yishun.lighting.R;
import co.yishun.lighting.api.model.Token;
import co.yishun.lighting.ui.common.BaseActivity;
import co.yishun.lighting.ui.view.AnimUtil;

/**
 * Created by carlos on 4/6/16.
 */
@EActivity
public class AccountActivity extends BaseActivity {
    public static final String TAG = "AccountActivity";
    @Extra
    String phone;
    @Extra("findPassword")
    boolean isFindPassword = false;

    protected void goToUserInfo(String phone, Token token) {
        AnimUtil.alpha(getSupportFragmentManager())
                .replace(R.id.container, UserInfoFragment_
                        .builder().phone(phone)
                        .token(token)
                        .editMode(UserInfoFragment.EDIT_MODE_COMMIT_LAST)
                        .build()).commitAllowingStateLoss();
    }

    protected void goToPassword(String phone, Token token) {
        AnimUtil.alpha(getSupportFragmentManager())
                .replace(R.id.container, PasswordFragment_
                        .builder().phone(phone).token(token)
                        .isFindPassword(isFindPassword).build())
                .commitAllowingStateLoss();
    }

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account);

        findViewById(R.id.container);

        getSupportFragmentManager().beginTransaction().
                replace(R.id.container,
                        SignUpFragment_
                                .builder().phone(phone)
                                .isFindPassword(isFindPassword).build())
                .commitAllowingStateLoss();
    }

    public interface PictureCroppedHandler {
        void onPictureSelectedFailed(Exception e);

        void onPictureCropped(Uri uri);
    }
}
