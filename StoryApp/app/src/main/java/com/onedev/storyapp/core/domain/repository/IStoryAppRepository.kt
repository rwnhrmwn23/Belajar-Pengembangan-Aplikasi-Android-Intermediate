package com.onedev.storyapp.core.domain.repository

import androidx.paging.PagingData
import com.onedev.storyapp.core.data.Resource
import com.onedev.storyapp.core.data.source.local.entity.StoryEntity
import com.onedev.storyapp.core.data.source.remote.request.RequestLogin
import com.onedev.storyapp.core.data.source.remote.request.RequestRegister
import com.onedev.storyapp.core.data.source.remote.response.AddStoryResponse
import com.onedev.storyapp.core.data.source.remote.response.GetStoryResponse
import com.onedev.storyapp.core.data.source.remote.response.ResponseLogin
import com.onedev.storyapp.core.data.source.remote.response.ResponseRegister
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IStoryAppRepository {
    fun register(request: RequestRegister): Flow<Resource<ResponseRegister>>
    fun login(request: RequestLogin): Flow<Resource<ResponseLogin>>

    fun story(page: Int, size: Int, location: Int): Flow<PagingData<StoryEntity>>
    fun storyMap(page: Int, size: Int, location: Int): Flow<Resource<GetStoryResponse>>
    fun story(file: MultipartBody.Part, description: RequestBody): Flow<Resource<AddStoryResponse>>
}