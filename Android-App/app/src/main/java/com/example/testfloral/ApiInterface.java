package com.example.testfloral;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("api/v1.0/recommendations/3")
    Call<List<Growth>> getGrowthInfo();

}
