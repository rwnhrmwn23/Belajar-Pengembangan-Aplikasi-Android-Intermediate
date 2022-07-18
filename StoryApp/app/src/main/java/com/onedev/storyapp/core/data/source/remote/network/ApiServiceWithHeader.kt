package com.onedev.storyapp.core.data.source.remote.network

import com.onedev.storyapp.core.data.source.remote.response.Story
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiServiceWithHeader {

    @GET("v1/stories")
    suspend fun story(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int,
    ): Story.GetResponse

    @Multipart
    @POST("v1/stories")
    suspend fun story(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ):  Story.PostResponse
}