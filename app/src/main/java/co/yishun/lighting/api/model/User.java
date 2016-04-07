package co.yishun.lighting.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import co.yishun.lighting.api.Account;

/**
 * Bean contains user info. <p> Created by Carlos on 2015/8/4.
 */
public class User implements Serializable {

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
        "avatar": ""
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
        "avatar": ""
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

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", id='" + id + '\'' +
                ", birthday='" + birthday + '\'' +
                ", weiboNickname='" + weiboNickname + '\'' +
                ", location='" + location + '\'' +
                ", sex='" + sex + '\'' +
                ", sexuality='" + sexuality + '\'' +
                ", wechatUid='" + wechatUid + '\'' +
                ", weiboUid='" + weiboUid + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", wechatNickname='" + wechatNickname + '\'' +
                ", portrait='" + portrait + '\'' +
                '}';
    }

    public Account.Gender getGender() {
        return Account.Gender.FEMALE;
    }
}
