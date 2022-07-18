package com.onedev.storyapp.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.onedev.storyapp.core.data.source.remote.RemoteDataSource
import com.onedev.storyapp.core.data.source.remote.network.ApiResponse
import com.onedev.storyapp.core.data.source.remote.response.Login
import com.onedev.storyapp.core.data.source.remote.response.Register
import com.onedev.storyapp.core.data.source.remote.response.Story
import com.onedev.storyapp.core.domain.repository.IStoryAppRepository
import com.onedev.storyapp.ui.fragment.story.StoryPagingSource
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

    override fun storyMap(
        page: Int,
        size: Int,
        location: Int
    ): Flow<Resource<Story.GetResponse>> =
        object : NetworkResource<Story.GetResponse>() {
            override suspend fun createCall(): Flow<ApiResponse<Story.GetResponse>> {
                return remoteDataSource.storyMap(page, size, location)
            }
        }.asFlow()

    override fun story(
        page: Int,
        size: Int,
        location: Int
    ): Flow<PagingData<Story.GetResponse.DataStory>> {
        return Pager(
            config = PagingConfig(5),
            pagingSourceFactory = {
                StoryPagingSource(remoteDataSource)
            }
        ).flow
    }

    override fun story(
        file: MultipartBody.Part,
        description: RequestBody
    ): Flow<Resource<Story.PostResponse>> =
        object : NetworkResource<Story.PostResponse>() {
            override suspend fun createCall(): Flow<ApiResponse<Story.PostResponse>> {
                return remoteDataSource.story(file, description)
            }
        }.asFlow()
}