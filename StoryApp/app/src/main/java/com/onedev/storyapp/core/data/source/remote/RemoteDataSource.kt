package com.onedev.storyapp.core.data.source.remote

import com.onedev.storyapp.core.data.source.remote.network.ApiResponse
import com.onedev.storyapp.core.data.source.remote.network.ApiService
import com.onedev.storyapp.core.data.source.remote.network.ApiServiceWithHeader
import com.onedev.storyapp.core.data.source.remote.response.Login
import com.onedev.storyapp.core.data.source.remote.response.Register
import com.onedev.storyapp.core.data.source.remote.response.Story
import com.onedev.storyapp.utils.ErrorUtils.getErrorThrowableMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RemoteDataSource(
    private val apiService: ApiService,
    private val apiServiceWithHeader: ApiServiceWithHeader
) {

    companion object {
        private const val TAG = "RemoteDataSource"
    }

    suspend fun register(request: Register.Request): Flow<ApiResponse<Register.Response>> {
        return flow {
            try {
                val response = apiService.register(request)
                if (!response.error)
                    emit(ApiResponse.Success(response))
                else
                    emit(ApiResponse.Error(response.message))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ApiResponse.Error(getErrorThrowableMsg(e)))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun login(request: Login.Request): Flow<ApiResponse<Login.Response>> {
        return flow {
            try {
                val response = apiService.login(request)
                if (!response.error)
                    emit(ApiResponse.Success(response))
                else
                    emit(ApiResponse.Error(response.message))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ApiResponse.Error(getErrorThrowableMsg(e)))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun story(): Flow<ApiResponse<Story.GetResponse>> {
        return flow {
            try {
                val response = apiServiceWithHeader.story()
                if (!response.error)
                    emit(ApiResponse.Success(response))
                else
                    emit(ApiResponse.Error(response.message))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ApiResponse.Error(getErrorThrowableMsg(e)))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun story(file: MultipartBody.Part, description: RequestBody): Flow<ApiResponse<Story.PostResponse>> {
        return flow {
            try {
                val response = apiServiceWithHeader.story(file, description)
                if (!response.error)
                    emit(ApiResponse.Success(response))
                else
                    emit(ApiResponse.Error(response.message))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ApiResponse.Error(getErrorThrowableMsg(e)))
            }
        }.flowOn(Dispatchers.IO)
    }
}