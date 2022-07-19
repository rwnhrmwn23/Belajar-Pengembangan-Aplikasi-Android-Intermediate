package com.onedev.storyapp.ui.fragment.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.onedev.storyapp.core.domain.usecase.StoryAppUseCase
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val storyAppUseCase: StoryAppUseCase) : ViewModel() {

    fun story(page: Int, size: Int, location: Int) =
        storyAppUseCase.story(page, size, location).cachedIn(viewModelScope).asLiveData()

    fun story(
        file: MultipartBody.Part, description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) = storyAppUseCase.story(file, description, lat, lon).asLiveData()
}