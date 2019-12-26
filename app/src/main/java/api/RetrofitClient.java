package api;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SYSTEM10 on 6/7/2018.
 */

public class RetrofitClient {
    private static final String ROOT_URL = "http://Kalbrojson.brainmagicllc.com";
    private static final String ROOT_UPLOAD_URL = "http://Kalbrojson.brainmagicllc.com/";

   /* private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }*/
    private static Retrofit getRetrofitInstance() {
        GsonConverterFactory gsonConverterFactory=GsonConverterFactory.create(new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create());
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
				.client(okClient())
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    private static OkHttpClient okClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.MINUTES)
                .writeTimeout(15, TimeUnit.MINUTES)
                .readTimeout(15, TimeUnit.MINUTES)
                .build();
    }

    private static Retrofit getRetrofitUplaodInstance() {
        return new Retrofit.Builder()
                .baseUrl(ROOT_UPLOAD_URL)
                .client(okClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Get API Service
     *
     * @return API Service
     */

    public static APIService getApiService() {
        return getRetrofitInstance().create(APIService.class);
    }
    public static APIService getApiUplaodService() {
        return getRetrofitUplaodInstance().create(APIService.class);
    }

}
