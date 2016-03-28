package co.yishun.lighting.api;

import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import com.google.gson.annotations.SerializedName;

import co.yishun.lighting.api.model.User;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Carlos on 2016/3/23.
 */
public interface Account {


    @POST("/register")
    @FormUrlEncoded
    void register(@Path("phone_number") String phoneNum, @Path("password") String password);

    @POST("/validate_sms")
    @FormUrlEncoded
    void validateSMS(@Path("phone_number") String phoneNum,
                     @Path("validate_code") String validateCode, @Path("validate_type") @ValidateType String type);

    @POST("/login")
    @FormUrlEncoded
    Call<User> login(@Path("phone_number") String phoneNum,
                     @Path("login_type") @LoginType String loginType,
                     @Path("password") @Nullable String password,
                     @Path("access_token") @Nullable String token
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
