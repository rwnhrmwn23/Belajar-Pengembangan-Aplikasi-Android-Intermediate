package com.onedev.storyapp.core.data.source.remote.network

import com.onedev.storyapp.core.data.source.remote.response.AddStoryResponse
import com.onedev.storyapp.core.data.source.remote.response.GetStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiServiceWithHeader {

    @GET("v1/stories")
    suspend fun story(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int,
    ): GetStoryResponse

    @Multipart
    @POST("v1/stories")
    suspend fun story(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?,
    ):  AddStoryResponse
}