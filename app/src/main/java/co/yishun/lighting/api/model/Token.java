package co.yishun.lighting.api.model;

import java.io.Serializable;

/**
 * Created by carlos on 4/6/16.
 */
public class Token implements Serializable {
    public String userId;
    public String accessToken;

    @Override
    public String toString() {
        return "Token{" +
                "userId='" + userId + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
