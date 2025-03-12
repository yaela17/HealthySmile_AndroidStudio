package com.example.healthysmile;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCliente {
    private static final String BASE_URL = "http://10.0.2.2:3000/";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ApiMySqlServicio getApiService() {
        return retrofit.create(ApiMySqlServicio.class);
    }
}
