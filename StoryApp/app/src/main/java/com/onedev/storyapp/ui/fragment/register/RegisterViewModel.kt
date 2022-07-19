package com.onedev.storyapp.ui.fragment.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.onedev.storyapp.core.data.source.remote.request.RequestLogin
import com.onedev.storyapp.core.data.source.remote.request.RequestRegister
import com.onedev.storyapp.core.domain.usecase.StoryAppUseCase

class RegisterViewModel(private val storyAppUseCase: StoryAppUseCase) : ViewModel() {
    fun register(request: RequestRegister) = storyAppUseCase.register(request).asLiveData()
    fun login(request: RequestLogin) = storyAppUseCase.login(request).asLiveData()
}