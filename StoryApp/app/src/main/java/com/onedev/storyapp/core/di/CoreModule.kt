package com.onedev.storyapp.core.di

import androidx.room.Room
import com.onedev.storyapp.BuildConfig.BASE_URL
import com.onedev.storyapp.BuildConfig.DB_NAME
import com.onedev.storyapp.core.data.StoryAppRepository
import com.onedev.storyapp.core.data.source.local.LocalDataSource
import com.onedev.storyapp.core.data.source.local.room.StoryDatabase
import com.onedev.storyapp.core.data.source.remote.RemoteDataSource
import com.onedev.storyapp.core.data.source.remote.network.ApiService
import com.onedev.storyapp.core.data.source.remote.network.ApiServiceWithHeader
import com.onedev.storyapp.core.domain.repository.IStoryAppRepository
import com.onedev.storyapp.core.domain.usecase.StoryAppInteractor
import com.onedev.storyapp.core.domain.usecase.StoryAppUseCase
import com.onedev.storyapp.ui.fragment.login.LoginViewModel
import com.onedev.storyapp.ui.fragment.maps.StoryLocationViewModel
import com.onedev.storyapp.ui.fragment.register.RegisterViewModel
import com.onedev.storyapp.ui.fragment.story.StoryViewModel
import com.onedev.storyapp.utils.Constant.USER_TOKEN
import com.onedev.storyapp.utils.getPreference
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<StoryDatabase>().storyDao() }
    factory { get<StoryDatabase>().remoteKeysDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            StoryDatabase::class.java,
            DB_NAME
        ).build()
    }
}

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
    single { LocalDataSource(get(), get()) }
    single<IStoryAppRepository> {
        StoryAppRepository(get(), get())
    }
}

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { StoryViewModel(get()) }
    viewModel { StoryLocationViewModel(get()) }
}