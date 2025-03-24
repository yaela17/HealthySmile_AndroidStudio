package com.example.healthysmile.repository;

import static com.example.healthysmile.service.URLSApisNode.BASE_URL_APIs_Node;

import com.example.healthysmile.service.ApiNodeMySqlService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NodeApiRetrofitClient {

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL_APIs_Node)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ApiNodeMySqlService getApiService() {
        return retrofit.create(ApiNodeMySqlService.class);
    }
}
