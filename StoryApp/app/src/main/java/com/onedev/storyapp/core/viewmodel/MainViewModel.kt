package com.onedev.storyapp.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.onedev.storyapp.core.data.source.remote.response.Register
import com.onedev.storyapp.core.domain.usecase.StoryAppUseCase

class MainViewModel(private val storyAppUseCase: StoryAppUseCase) : ViewModel() {
    fun register(request: Register.Request) = storyAppUseCase.register(request).asLiveData()
}