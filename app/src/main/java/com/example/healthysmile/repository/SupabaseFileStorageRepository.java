package com.example.healthysmile.repository;

import okhttp3.*;

import java.io.File;
import java.io.IOException;

public class SupabaseFileStorageRepository {
    private static final String SUPABASE_URL = "https://numnevpjhspclytqdrrs.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im51bW5ldnBqaHNwY2x5dHFkcnJzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDE0OTEzMzYsImV4cCI6MjA1NzA2NzMzNn0.ApL13cK5F-5obMcBctjfHo5Up1F6yllXsOvwH6bVrWQ";
    private static final String BUCKET_NAME = "archivos";

    private final OkHttpClient client = new OkHttpClient();

    public String uploadFile(File file) throws IOException {
        String uploadUrl = SUPABASE_URL + "/storage/v1/object/" + BUCKET_NAME + "/" + file.getName();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(file, MediaType.parse("application/octet-stream")))
                .build();

        Request request = new Request.Builder()
                .url(uploadUrl)
                .header("Authorization", "Bearer " + SUPABASE_KEY)
                .header("apikey", SUPABASE_KEY)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        return response.body() != null ? response.body().string() : "Error al subir archivo";
    }
}
