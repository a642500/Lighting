package co.yishun.lighting.api;

import android.support.annotation.StringDef;

import java.util.List;

import co.yishun.lighting.api.model.AudioMedia;
import co.yishun.lighting.api.model.OtherUser;
import co.yishun.lighting.api.model.Question;
import co.yishun.lighting.api.model.VideoMedia;
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
            @Field("class") @QuestionType String type
    );


    @FormUrlEncoded
    @POST("procedure/get_user_id_by_love_sex")
    Call<OtherUser> getAUser(@Field("user_id") String userId,
                             @Field("access_token") String accessToken,
                             @Field("love_sex") @Account.SexualityType String loveSex);

    @FormUrlEncoded
    @POST("procedure/get_user_ids_by_love_sex")
    Call<OtherUser> getSomeUsers(@Field("user_id") String userId,
                                 @Field("access_token") String accessToken,
                                 @Field("love_sex") @Account.SexualityType String loveSex);

    @FormUrlEncoded
    @POST("procedure/get_media")
    Call<AudioMedia> getAudio(
            @Field("user_id") String userId,
            @Field("access_token") String accessToken,
            @Field("target_id") String targetId,
            @Field("audio_class") @Media.AudioType String audioType,
            @Field("file_type") @Media.MediaType String type
    );

    @FormUrlEncoded
    @POST("procedure/get_media")
    Call<VideoMedia> getVideo(
            @Field("user_id") String userId,
            @Field("access_token") String accessToken,
            @Field("target_id") String targetId,
            @Field("class") @Media.VideoType String videoType,
            @Field("file_type") @Media.MediaType String type
    );

    @FormUrlEncoded
    @POST("procedure/light_up")
    Call<Void> lightUp(
            @Field("user_id") String userId,
            @Field("access_token") String accessToken,
            @Field("target_id") String targetId,
            @Field("order") int order,
            @Field("light_status") int isOn
    );

    @StringDef({QUESTION_TYPE_INFO,
            QUESTION_TYPE_EXPERIENCE,
            QUESTION_TYPE_VALUES})
    @interface QuestionType {
    }
}
