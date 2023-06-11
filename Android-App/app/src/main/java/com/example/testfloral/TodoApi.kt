package com.example.testfloral

import retrofit2.Response
import retrofit2.http.GET

interface TodoApi {

    @GET("/api/v1.0/demand/3")
    suspend fun getTodos(): Response<List<Todo>>

    //3,4,7
    @GET("/api/v1.0/recommendations/3")
    suspend fun getRec(): Response<List<RacAlgorithm>>

    @GET("/api/v1.0/fav/3")
    suspend fun getFav(): Response<List<FavList>>

}