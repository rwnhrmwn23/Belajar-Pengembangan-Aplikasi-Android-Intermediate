package com.onedev.storyapp.ui.fragment.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.onedev.storyapp.core.domain.usecase.StoryAppUseCase

class StoryLocationViewModel(private val storyAppUseCase: StoryAppUseCase) : ViewModel() {
    fun storyMap(page: Int, size: Int, location: Int) = storyAppUseCase.storyMap(page, size, location).asLiveData()
}