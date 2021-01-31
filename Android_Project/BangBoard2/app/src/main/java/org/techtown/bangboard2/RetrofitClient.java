package org.techtown.bangboard2;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private Retrofit retrofit;
    private ApiService apiService;
    private static RetrofitClient retrofitClient = new RetrofitClient();

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static RetrofitClient getInstance() {
        return retrofitClient;
    }

    public ApiService getApiService() {
        return apiService;
    }
}
