package com.onedev.storyapp.core.domain.usecase

import com.onedev.storyapp.core.data.source.remote.request.RequestLogin
import com.onedev.storyapp.core.data.source.remote.request.RequestRegister
import com.onedev.storyapp.core.domain.repository.IStoryAppRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryAppInteractor(private val iStoryAppRepository: IStoryAppRepository) : StoryAppUseCase {
    override fun register(request: RequestRegister) = iStoryAppRepository.register(request)
    override fun login(request: RequestLogin) = iStoryAppRepository.login(request)

    override fun story(page: Int, size: Int, location: Int) = iStoryAppRepository.story(page, size, location)
    override fun storyMap(page: Int, size: Int, location: Int) = iStoryAppRepository.storyMap(page, size, location)
    override fun story(file: MultipartBody.Part, description: RequestBody) = iStoryAppRepository.story(file, description)
}