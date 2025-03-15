package com.example.healthysmile.repository;

import com.example.healthysmile.service.ApiNodeMySqlService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NodeApiRetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:3000/";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ApiNodeMySqlService getApiService() {
        return retrofit.create(ApiNodeMySqlService.class);
    }
}
