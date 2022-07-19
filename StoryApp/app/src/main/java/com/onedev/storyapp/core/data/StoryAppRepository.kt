package com.onedev.storyapp.core.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.onedev.storyapp.core.data.paging.StoryRemoteMediator
import com.onedev.storyapp.core.data.source.local.LocalDataSource
import com.onedev.storyapp.core.data.source.local.entity.StoryEntity
import com.onedev.storyapp.core.data.source.remote.RemoteDataSource
import com.onedev.storyapp.core.data.source.remote.network.ApiResponse
import com.onedev.storyapp.core.data.source.remote.request.RequestLogin
import com.onedev.storyapp.core.data.source.remote.request.RequestRegister
import com.onedev.storyapp.core.data.source.remote.response.AddStoryResponse
import com.onedev.storyapp.core.data.source.remote.response.GetStoryResponse
import com.onedev.storyapp.core.data.source.remote.response.ResponseLogin
import com.onedev.storyapp.core.data.source.remote.response.ResponseRegister
import com.onedev.storyapp.core.domain.repository.IStoryAppRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryAppRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IStoryAppRepository {

    override fun register(request: RequestRegister): Flow<Resource<ResponseRegister>> =
        object : NetworkResource<ResponseRegister>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseRegister>> {
                return remoteDataSource.register(request)
            }
        }.asFlow()

    override fun login(request: RequestLogin): Flow<Resource<ResponseLogin>> =
        object : NetworkResource<ResponseLogin>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseLogin>> {
                return remoteDataSource.login(request)
            }
        }.asFlow()

    override fun storyMap(
        page: Int,
        size: Int,
        location: Int
    ): Flow<Resource<GetStoryResponse>> =
        object : NetworkResource<GetStoryResponse>() {
            override suspend fun createCall(): Flow<ApiResponse<GetStoryResponse>> {
                return remoteDataSource.storyMap(page, size, location)
            }
        }.asFlow()

    @OptIn(ExperimentalPagingApi::class)
    override fun story(
        page: Int,
        size: Int,
        location: Int
    ): Flow<PagingData<StoryEntity>> {
        return Pager(
            config = PagingConfig(5),
            remoteMediator = StoryRemoteMediator(remoteDataSource, localDataSource),
            pagingSourceFactory = {
                localDataSource.getAllStory()
            }
        ).flow
    }

    override fun story(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<Resource<AddStoryResponse>> =
        object : NetworkResource<AddStoryResponse>() {
            override suspend fun createCall(): Flow<ApiResponse<AddStoryResponse>> {
                return remoteDataSource.story(file, description, lat, lon)
            }
        }.asFlow()
}