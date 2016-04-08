package co.yishun.lighting.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by carlos on 4/8/16.
 */
public class Question implements Serializable {
    @SerializedName("problem_content")
    String content;

    @SerializedName("problem_id")
    String id;
}
