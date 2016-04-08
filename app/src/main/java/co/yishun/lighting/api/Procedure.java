package co.yishun.lighting.api;

import android.support.annotation.StringDef;

import java.util.List;

import co.yishun.lighting.api.model.Question;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by carlos on 4/8/16.
 */
public interface Procedure {
    String QUESTION_TYPE_INFO = "basic_info";
    String QUESTION_TYPE_EXPERIENCE = "emotion_experience";
    String QUESTION_TYPE_VALUES = "value_concept";

    @POST("procedure/get_questions")
    @FormUrlEncoded
    Call<List<Question>> getQuestions(
            @Field("user_id") String userId,
            @Field("access_token") String accessToken,
            @Field("class") @QuestionType String type,
            @Field("limit") int limit
    );

    @StringDef({QUESTION_TYPE_INFO,
            QUESTION_TYPE_EXPERIENCE,
            QUESTION_TYPE_VALUES})
    @interface QuestionType {
    }
}
