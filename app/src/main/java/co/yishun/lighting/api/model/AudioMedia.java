package co.yishun.lighting.api.model;

/**
 * Created by carlos on 4/30/16.
 */
public class AudioMedia extends Question {

    public String saveKey;

    @Override
    public String toString() {
        return "AudioMedia{" +
                "saveKey='" + saveKey + '\'' +
                "content='" + content + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
