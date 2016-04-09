package co.yishun.lighting.api.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Field;

import co.yishun.lighting.api.Account;

/**
 * Bean contains user info. <p> Created by Carlos on 2015/8/4.
 */
public class User extends JsonBean implements Serializable {

    /*
{
    "data": {
        "wechat_uid": "",
        "weibo_uid": "",
        "nickname": "",
        "_id": "56f6625d333cd1a3808e6145",
        "love_sex": "",
        "phone_number": "15820108996",
        "access_token": "19f6ebab161aad4a7f2eb6195df0a50c",
        "birthday": "",
        "weibo_nickname": "",
        "location": "",
        "wechat_nickname": "",
        "sex": "",
        "portrait": ""
    }
}

{
    "data": {
        "wechat_uid": "",
        "value_concept_light_number": 0,
        "weibo_uid": "",
        "nickname": "Coordinate35",
        "sex": "",
        "love_sex": "",
        "emotion_experience_light_number": 0,
        "birthday": "",
        "phone_number": "15172354423",
        "access_token": "87ec2279c7b10a1c6f0a41e60349aa46",
        "age": 0,
        "weibo_nickname": "",
        "location": "",
        "wechat_nickname": "",
        "basic_info_light_number": 0,
        "portrait": ""
    }
}

     */


    public String nickname;
    @SerializedName("_id")
    public String id;
    public String birthday;
    public String weiboNickname;
    public String location;
    public String sex;
    @SerializedName("love_sex")
    public String sexuality;
    public String wechatUid;
    public String weiboUid;
    public String phoneNumber;
    public String accessToken;
    public String wechatNickname;
    public String portrait;

    public static User dummyUser() {
        User user = new User();
        user.nickname = "nickname";
        user.portrait = "http://ss.bdimg.com/static/superman/img/logo/bd_logo1_31bdc765.png";
        user.setSex(Account.Gender.OTHER);
        user.setSexuality(Account.Gender.OTHER);
        return user;
    }

    public Token getToken() {
        Token token = new Token();
        token.userId = id;
        token.accessToken = accessToken;
        return token;
    }

    public Account.Gender getSex() {
        return Account.Gender.MALE;
    }

    public void setSex(Account.Gender gender) {
        this.sex = gender.toString();
    }

    public Account.Gender getSexuality() {
        return Account.Gender.FEMALE;
    }

    public void setSexuality(Account.Gender gender) {
        this.sexuality = gender.toString();
    }

    public void add(@Nullable User user) {
        if (user == null) {
            return;
        }
        try {
            for (Field field : User.class.getDeclaredFields()) {
                field.setAccessible(true);
                String value = (String) field.get(user);
                if (value != null) {
                    field.set(this, value);
                }
            }
        } catch (IllegalAccessException ignore) {
        }
    }
}
