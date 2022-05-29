package com.onedev.storyapp.core.data.source.remote.network

import com.onedev.storyapp.core.data.source.remote.response.Story
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServiceWithHeader {

    @GET("v1/stories")
    suspend fun story(): Story.GetResponse

    @Multipart
    @POST("v1/stories")
    suspend fun story(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ):  Story.PostResponse
}