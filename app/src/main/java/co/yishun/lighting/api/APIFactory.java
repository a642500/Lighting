package co.yishun.lighting.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import co.yishun.lighting.BuildConfig;
import co.yishun.lighting.Constants;
import co.yishun.lighting.api.model.Token;
import co.yishun.lighting.api.model.User;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by carlos on 3/26/16.
 */
public class APIFactory {
    private final static Gson mGson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private final static Gson mDeserializerGson = new GsonBuilder()
            .registerTypeAdapter(User.class, new DataDeserializer<>())
            .registerTypeAdapter(Token.class, new DataDeserializer<>())
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private static Retrofit mRetrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(mDeserializerGson)).client(getOkHttpClient()).build();

    public static Account getAccountAPI() {
        return mRetrofit.create(Account.class);
    }

    public static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //noinspection PointlessBooleanExpression
        if (Constants.LOG_ENABLE || BuildConfig.DEBUG) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        return builder.build();
    }

    public static class DataDeserializer<T> implements JsonDeserializer<T> {

        @Override
        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            // Get the "content" element from the parsed JSON
            JsonElement content = json.getAsJsonObject().get("data");

            // Deserialize it. You use a new instance of Gson to avoid infinite recursion
            // to this deserializer
            return mGson.fromJson(content, typeOfT);
        }
    }

}
