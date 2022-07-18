package com.onedev.storyapp.ui.fragment.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.onedev.storyapp.core.data.source.remote.response.Login
import com.onedev.storyapp.core.data.source.remote.response.Register
import com.onedev.storyapp.core.domain.usecase.StoryAppUseCase
import okhttp3.MultipartBody
import okhttp3.RequestBody

class LoginViewModel(private val storyAppUseCase: StoryAppUseCase) : ViewModel() {
    fun login(request: Login.Request) = storyAppUseCase.login(request).asLiveData()
}