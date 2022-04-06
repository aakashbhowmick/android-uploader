package com.example.androidexplore

import retrofit2.Call
import retrofit2.http.GET

interface APIInterface {
    @GET("/")
    fun doGetListResources(): Call<APIResponse?>?
}