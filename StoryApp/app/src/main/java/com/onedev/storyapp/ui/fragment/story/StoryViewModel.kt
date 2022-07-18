package com.onedev.storyapp.ui.fragment.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.onedev.storyapp.core.data.source.remote.response.Login
import com.onedev.storyapp.core.data.source.remote.response.Register
import com.onedev.storyapp.core.domain.usecase.StoryAppUseCase
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val storyAppUseCase: StoryAppUseCase) : ViewModel() {
    fun story() = storyAppUseCase.story().asLiveData()
    fun story(file: MultipartBody.Part, description: RequestBody) = storyAppUseCase.story(file, description).asLiveData()
}