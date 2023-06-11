package com.example.testfloral;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://192.168.1.3:5000/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofit;
    }
}
