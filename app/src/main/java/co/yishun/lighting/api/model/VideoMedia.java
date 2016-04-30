package co.yishun.lighting.api.model;

import java.util.List;

/**
 * Created by carlos on 4/30/16.
 */
public class VideoMedia {

    public String saveKey;
    public List<Question> questions;

    @Override
    public String toString() {
        return "VideoMedia{" +
                "saveKey='" + saveKey + '\'' +
                ", questions=" + questions +
                '}';
    }
}
