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

    String FILE_TYPE_AUDIO = "audio";
    String FILE_TYPE_VIDEO = "video";
    String AUDIO_TYPE_INFO = "basic_info";

    @POST("media/upload_request")
    @FormUrlEncoded
    Call<Void> getQuestions(
            @Field("user_id") String userId,
            @Field("sex") String sex,
            @Field("file_type") @FileType String type,
            @Field("audio_class") @AudioType String type,
            @Field("question_arr") int limit
    );

    @StringDef({FILE_TYPE_AUDIO, FILE_TYPE_VIDEO})
    @interface FileType {
    }

    @StringDef({AUDIO_TYPE_INFO})
    @interface AudioType {
    }
}
