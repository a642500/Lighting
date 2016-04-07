package co.yishun.lighting.api.model;

import java.io.Serializable;

/**
 * Created by carlos on 4/6/16.
 */
public class Token implements Serializable {
    private Data data;

    @Override
    public String toString() {
        return "Token{" +
                "userId='" + data.userId + '\'' +
                ", accessToken='" + data.accessToken + '\'' +
                '}';
    }

    public String getAccessToken() {
        return data.accessToken;
    }

    public void setAccessToken(String accessToken) {
        data.accessToken = accessToken;
    }

    public String getUserId() {
        return data.userId;
    }

    public void setUserId(String userId) {
        data.userId = userId;
    }


    private static class Data {
        public String userId;
        public String accessToken;
    }
}
