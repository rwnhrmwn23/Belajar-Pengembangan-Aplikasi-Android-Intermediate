package com.onedev.storyapp.core.di

import com.onedev.storyapp.BuildConfig.BASE_URL
import com.onedev.storyapp.core.data.StoryAppRepository
import com.onedev.storyapp.core.data.source.remote.RemoteDataSource
import com.onedev.storyapp.core.data.source.remote.network.ApiService
import com.onedev.storyapp.core.data.source.remote.network.ApiServiceWithHeader
import com.onedev.storyapp.core.domain.repository.IStoryAppRepository
import com.onedev.storyapp.core.domain.usecase.StoryAppInteractor
import com.onedev.storyapp.core.domain.usecase.StoryAppUseCase
import com.onedev.storyapp.core.viewmodel.MainViewModel
import com.onedev.storyapp.utils.Constant.USER_TOKEN
import com.onedev.storyapp.utils.getPreference
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("Authorization", getPreference(get(), USER_TOKEN))
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiServiceWithHeader::class.java)
    }
}

val useCaseModule = module {
    factory<StoryAppUseCase> { StoryAppInteractor(get()) }
}

val repositoryModule = module {
    single { RemoteDataSource(get(), get()) }
    single<IStoryAppRepository> {
        StoryAppRepository(get())
    }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}