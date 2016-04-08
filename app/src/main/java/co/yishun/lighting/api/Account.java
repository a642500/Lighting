package co.yishun.lighting.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.annotation.StringRes;

import com.google.gson.annotations.SerializedName;

import co.yishun.lighting.R;
import co.yishun.lighting.account.UserInfo;
import co.yishun.lighting.api.model.Token;
import co.yishun.lighting.api.model.UploadToken;
import co.yishun.lighting.api.model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Carlos on 2016/3/23.
 */
public interface Account {


    @POST("account/register")
    @FormUrlEncoded
    Call<Void> register(@Field("phone_number") String phoneNum);

    @POST("account/validate_sms")
    @FormUrlEncoded
    Call<Token> validateSMS(@Field("phone_number") String phoneNum,
                            @Field("validate_code") String validateCode,
                            @Field("validate_type") @ValidateType String type);

    @POST("account/login")
    @FormUrlEncoded
    Call<User> login(@Field("phone_number") String phoneNum,
                     @Field("login_type") @LoginType String loginType,
                     @Field("password") @Nullable String password,
                     @Field("access_token") @Nullable String token
    );

    @FormUrlEncoded
    @POST("account/change_personal_info")
    Call<Void> changePersonalInfo(
            @Field("user_id") String userId,
            @Field("access_token") String accessToken,
            @Field("personal_info") @NonNull User userInfo);

    @FormUrlEncoded
    @POST("account/get_user_info")
    Call<UserInfo> getUserInfo(@Field("user_id") String userId,
                               @Field("access_token") String accessToken,
                               @Field("target_id") String targetId);

    @FormUrlEncoded
    @POST("account/change_password")
    Call<Void> changePassword(@Field("phone_number") String phoneNum,
                              @Field("password") String password
    );

    @FormUrlEncoded
    @POST("account/refresh_access_token")
    Call<Token> refreshToken(@Field("user_id") String userId,
                             @Field("access_token") String accessToken);


    @FormUrlEncoded
    @POST("account/upload_portrait_request")
    Call<UploadToken> uploadPortraitRequest(@Field("user_id") String userId,
                                            @Field("access_token") String accessToken,
                                            @Field("save_key") String saveKey,
                                            @Field("expiration") String expiration,
                                            @Field("bucket") String bucket
    );

    @FormUrlEncoded
    @POST("account/upload_portrait_confirm")
    Call<Void> uploadPortraitConfirm(@Field("user_id") String userId,
                                     @Field("access_token") String accessToken);

    @FormUrlEncoded
    @POST("account/refresh_device_token")
    Call<Void> refreshDeviceToken(@Field("user_id") String userId,
                                  @Field("access_token") String accessToken,
                                  @Field("device_token") String deviceToken
    );


    enum Gender {
        @SerializedName("f")
        FEMALE,
        @SerializedName("m")
        MALE,
        @SerializedName("n")
        OTHER,;
        @StringRes
        public static final int DISPLAY_TEXT_RES[] = new int[]{
                R.string.api_gender_display_female,
                R.string.api_gender_display_male,
                R.string.api_gender_display_Unknown
        };

        public static Gender format(String s) {
            switch (s) {
                case "f":
                    return FEMALE;
                case "m":
                    return MALE;
                default:
                    return OTHER;
            }
        }

        public static Gender format(int i) {
            switch (i) {
                case 0:
                    return FEMALE;
                case 1:
                    return MALE;
                default:
                    return OTHER;
            }
        }

        public String getDisplayText(Context context) {
            return context.getString(DISPLAY_TEXT_RES[toInt()]);
        }

        @Override
        public String toString() {
            switch (this) {
                case FEMALE:
                    return "f";
                case MALE:
                    return "m";
                default:
                    return "n";
            }
        }

        public int toInt() {
            switch (this) {
                case FEMALE:
                    return 0;
                case MALE:
                    return 1;
                default:
                    return 2;
            }
        }

    }

    @StringDef({"register", "change_password"})
    @interface ValidateType {
    }

    @StringDef({"password", "access_token"})
    @interface LoginType {
    }
}
