package com.onedev.storyapp.ui.fragment.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.onedev.storyapp.core.data.source.remote.request.RequestLogin
import com.onedev.storyapp.core.domain.usecase.StoryAppUseCase

class LoginViewModel(private val storyAppUseCase: StoryAppUseCase) : ViewModel() {
    fun login(request: RequestLogin) = storyAppUseCase.login(request).asLiveData()
}