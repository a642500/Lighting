package co.yishun.lighting.api;

import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import com.google.gson.annotations.SerializedName;

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
    void validateSMS(@Field("phone_number") String phoneNum,
                     @Field("validate_code") String validateCode, @Field("validate_type") @ValidateType String type);

    @POST("account/login")
    @FormUrlEncoded
    Call<User> login(@Field("phone_number") String phoneNum,
                     @Field("login_type") @LoginType String loginType,
                     @Field("password") @Nullable String password,
                     @Field("access_token") @Nullable String token
    );


    enum Gender {
        @SerializedName("f")
        FEMALE,
        @SerializedName("m")
        MALE,
        @SerializedName("n")
        OTHER,;

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
