package co.yishun.lighting.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by carlos on 4/8/16.
 */
public class QuestionList extends JsonBean implements Serializable {
    public List<Question> questions;
}
