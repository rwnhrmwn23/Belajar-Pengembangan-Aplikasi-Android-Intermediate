package com.onedev.storyapp.core.data

import com.onedev.storyapp.core.data.source.remote.RemoteDataSource
import com.onedev.storyapp.core.data.source.remote.network.ApiResponse
import com.onedev.storyapp.core.data.source.remote.response.Login
import com.onedev.storyapp.core.data.source.remote.response.Register
import com.onedev.storyapp.core.data.source.remote.response.Story
import com.onedev.storyapp.core.domain.repository.IStoryAppRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryAppRepository(
    private val remoteDataSource: RemoteDataSource
) : IStoryAppRepository {

    override fun register(request: Register.Request): Flow<Resource<Register.Response>> =
        object : NetworkResource<Register.Response>() {
            override suspend fun createCall(): Flow<ApiResponse<Register.Response>> {
                return remoteDataSource.register(request)
            }
        }.asFlow()

    override fun login(request: Login.Request): Flow<Resource<Login.Response>> =
        object : NetworkResource<Login.Response>() {
            override suspend fun createCall(): Flow<ApiResponse<Login.Response>> {
                return remoteDataSource.login(request)
            }
        }.asFlow()

    override fun story(): Flow<Resource<Story.GetResponse>> =
        object : NetworkResource<Story.GetResponse>() {
            override suspend fun createCall(): Flow<ApiResponse<Story.GetResponse>> {
                return remoteDataSource.story()
            }
        }.asFlow()

    override fun storyWithLocation(): Flow<Resource<Story.GetResponse>> =
        object : NetworkResource<Story.GetResponse>() {
            override suspend fun createCall(): Flow<ApiResponse<Story.GetResponse>> {
                return remoteDataSource.storyWithLocation()
            }
        }.asFlow()

    override fun story(file: MultipartBody.Part, description: RequestBody): Flow<Resource<Story.PostResponse>> =
        object : NetworkResource<Story.PostResponse>() {
            override suspend fun createCall(): Flow<ApiResponse<Story.PostResponse>> {
                return remoteDataSource.story(file, description)
            }
        }.asFlow()
}