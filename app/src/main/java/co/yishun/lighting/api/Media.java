package co.yishun.lighting.api;

import android.support.annotation.StringDef;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by carlos on 4/8/16.
 */
public interface Media {

    String MEDIA_TYPE_AUDIO = "audio";
    String MEDIA_TYPE_VIDEO = "video";

    @POST("media/upload_request")
    @FormUrlEncoded
    Call<Void> uploadAudioRequest(
            @Field("user_id") String userId,
            @Field("sex") String sex,
            @Field("access_token") String accessToken,
            @Field("file_type") @MediaType String fileType,
            @Field("audio_class") @AudioType String audioType,
            @Field("question_arr") String questionArray
    );


    @POST("media/upload_request")
    @FormUrlEncoded
    Call<Void> uploadVideoRequest(
            @Field("user_id") String userId,
            @Field("sex") String sex,
            @Field("access_token") String accessToken,
            @Field("file_type") @MediaType String fileType,
            @Field("video_class") @VideoType String audioType,
            @Field("questions") String questions,
            @Field("save_key") String saveKey
    );


    @FormUrlEncoded
    @POST("media/confirm")
    Call<Void> comfirm(
            @Field("user_id") String userId,
            @Field("access_token") String accessToken,
            @Field("file_type") @MediaType String fileType,
            @Field("file_id") String id
    );


    @StringDef({MEDIA_TYPE_AUDIO, MEDIA_TYPE_VIDEO})
    @interface MediaType {
    }

    @StringDef({Procedure.QUESTION_TYPE_INFO})
    @interface AudioType {
    }

    @StringDef({Procedure.QUESTION_TYPE_EXPERIENCE, Procedure.QUESTION_TYPE_VALUES})
    @interface VideoType {
    }
}
