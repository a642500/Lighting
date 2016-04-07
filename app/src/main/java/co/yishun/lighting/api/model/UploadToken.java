package co.yishun.lighting.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by carlos on 4/7/16.
 */
public class UploadToken implements Serializable {

    public String policy;
    @SerializedName(value = "signature", alternate = "signatrue")
    public String signature;
}
