package com.onedev.storyapp.core.domain.usecase

import com.onedev.storyapp.core.data.source.remote.response.Login
import com.onedev.storyapp.core.data.source.remote.response.Register
import com.onedev.storyapp.core.domain.repository.IStoryAppRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryAppInteractor(private val iStoryAppRepository: IStoryAppRepository) :
    StoryAppUseCase {
    override fun register(request: Register.Request) = iStoryAppRepository.register(request)
    override fun login(request: Login.Request) = iStoryAppRepository.login(request)
    override fun story() = iStoryAppRepository.story()
    override fun story(file: MultipartBody.Part, description: RequestBody) = iStoryAppRepository.story(file, description)
}