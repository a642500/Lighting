package co.yishun.lighting.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import co.yishun.lighting.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by carlos on 3/26/16.
 */
public class APIFactory {

    private static Retrofit mRetrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create())).client(getOkHttpClient()).build();

    public static Account getAccountAPI() {
        return mRetrofit.create(Account.class);
    }

    public static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder().build();
    }
}
