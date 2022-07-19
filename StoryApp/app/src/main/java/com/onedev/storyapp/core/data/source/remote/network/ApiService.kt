package com.onedev.storyapp.core.data.source.remote.network

import com.onedev.storyapp.core.data.source.remote.request.RequestLogin
import com.onedev.storyapp.core.data.source.remote.request.RequestRegister
import com.onedev.storyapp.core.data.source.remote.response.*
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("v1/register")
    suspend fun register(
        @Body request: RequestRegister
    ): ResponseRegister

    @POST("v1/login")
    suspend fun login(
        @Body request: RequestLogin
    ): ResponseLogin
}