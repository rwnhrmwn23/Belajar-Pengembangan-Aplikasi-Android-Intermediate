package com.onedev.storyapp.core.data.source.remote

import android.content.Context
import com.onedev.storyapp.MyApplication
import com.onedev.storyapp.core.data.source.remote.network.ApiResponse
import com.onedev.storyapp.core.data.source.remote.network.ApiService
import com.onedev.storyapp.core.data.source.remote.network.ApiServiceWithHeader
import com.onedev.storyapp.core.data.source.remote.request.RequestLogin
import com.onedev.storyapp.core.data.source.remote.request.RequestRegister
import com.onedev.storyapp.core.data.source.remote.response.*
import com.onedev.storyapp.utils.Constant
import com.onedev.storyapp.utils.ErrorUtils.getErrorThrowableMsg
import com.onedev.storyapp.utils.putListPreference
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

    private lateinit var listImage: ArrayList<String>

    suspend fun register(request: RequestRegister): Flow<ApiResponse<ResponseRegister>> {
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

    suspend fun login(request: RequestLogin): Flow<ApiResponse<ResponseLogin>> {
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

    suspend fun story(page: Int, size: Int, location: Int): Flow<ApiResponse<GetStoryResponse>> {
        return flow {
            try {
                val response = apiServiceWithHeader.story(page, size, location)
                if (!response.error) {
                    emit(ApiResponse.Success(response))

                    /*
                    * Input list image to show in stack widget
                    * */
                    listImage = ArrayList()
                    for (i in response.listStory) {
                        listImage.addAll(listOf(i.photoUrl))
                    }
                    putListPreference(
                        MyApplication.appContext as Context,
                        Constant.LIST_STRING,
                        listImage
                    )
                } else
                    emit(ApiResponse.Error(response.message))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ApiResponse.Error(getErrorThrowableMsg(e)))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun storyMap(page: Int, size: Int, location: Int): Flow<ApiResponse<GetStoryResponse>> {
        return flow {
            try {
                val response = apiServiceWithHeader.story(page, size, location)
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

    suspend fun story(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<ApiResponse<AddStoryResponse>> {
        return flow {
            try {
                val response = apiServiceWithHeader.story(file, description, lat, lon)
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