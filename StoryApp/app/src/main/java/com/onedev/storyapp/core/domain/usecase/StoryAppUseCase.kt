package com.onedev.storyapp.core.domain.usecase

import com.onedev.storyapp.core.data.Resource
import com.onedev.storyapp.core.data.source.remote.response.Login
import com.onedev.storyapp.core.data.source.remote.response.Register
import com.onedev.storyapp.core.data.source.remote.response.Story
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryAppUseCase {
    fun register(request: Register.Request): Flow<Resource<Register.Response>>
    fun login(request: Login.Request): Flow<Resource<Login.Response>>
    fun story(): Flow<Resource<Story.GetResponse>>
    fun story(file: MultipartBody.Part, description: RequestBody): Flow<Resource<Story.PostResponse>>
}