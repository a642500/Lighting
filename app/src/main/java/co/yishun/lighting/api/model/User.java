package co.yishun.lighting.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Bean contains user info. <p> Created by Carlos on 2015/8/4.
 */
public class User {

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
     */


    public String nickname;
    @SerializedName("_id")
    public String id;
    public String birthday;
    public String weiboNickname;
    public String location;
    public String sex;
    public String loveSex;
    public String wechatUid;
    public String weiboUid;
    public String phoneNumber;
    public String accessToken;
    public String wechatNickname;
    public String portrait;

    public String avatarUrl;//TODO lack

}
