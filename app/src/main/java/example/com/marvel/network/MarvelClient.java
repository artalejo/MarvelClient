package example.com.marvel.network;

import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;

import example.com.marvel.network.models.MarvelCharactersResponse;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MarvelClient {

    public static final String REST_API_URL = "http://gateway.marvel.com/v1/public/";
    private static final String TS = "1";
    private static final String PUBLIC_KEY = "72bde2a90a511d19352f3f4994e404aa";
    private static final String PRIVATE_KEY = "233bd9870a3a6a0859d6c5ed21b48e836d721249";
    private static final String HASH = "719b890d36460dde99f439266f58079e";

    private static Retrofit retrofit;
    private static MarvelServicesInterface apiService;

    public static MarvelServicesInterface getApiClient() {

        if (apiService == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .addInterceptor(getApiCredentialsInterceptor())
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(REST_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            apiService = retrofit.create(MarvelServicesInterface.class);
        }

        return apiService;
    }

    @NonNull
    private static Interceptor getApiCredentialsInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("ts", TS)
                        .addQueryParameter("apikey", PUBLIC_KEY)
                        .addQueryParameter("hash", HASH)
                        .build();

                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
    }


    public interface MarvelServicesInterface {

        @GET("characters")
        Call<MarvelCharactersResponse> getCharacters(@Query("limit") int limit, @Query("offset") long characterOffset);

    }
}
