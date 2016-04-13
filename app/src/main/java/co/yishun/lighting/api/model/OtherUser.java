package co.yishun.lighting.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by carlos on 4/13/16.
 */
public class OtherUser extends JsonBean {
    public String targetId;
    @SerializedName("have_light_number")
    public String lightNumber;
}
