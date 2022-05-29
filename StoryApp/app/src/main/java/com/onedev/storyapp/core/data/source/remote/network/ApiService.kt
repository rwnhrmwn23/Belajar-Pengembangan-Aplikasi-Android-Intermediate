package com.onedev.storyapp.core.data.source.remote.network

import com.onedev.storyapp.core.data.source.remote.response.Login
import com.onedev.storyapp.core.data.source.remote.response.Register
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("v1/register")
    suspend fun register(
        @Body request: Register.Request
    ): Register.Response

    @POST("v1/login")
    suspend fun login(
        @Body request: Login.Request
    ): Login.Response
}