package co.yishun.lighting.api;

import co.yishun.lighting.Constants;
import retrofit2.Retrofit;

/**
 * Created by carlos on 3/26/16.
 */
public class APIFactory {

    private static Retrofit mRetrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).build();

    public Account getAccountAPI() {
        return mRetrofit.create(Account.class);
    }
}
