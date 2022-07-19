package com.onedev.storyapp.utils

import com.onedev.storyapp.core.data.source.local.entity.StoryEntity
import com.onedev.storyapp.core.data.source.remote.response.GetStoryResponse

object Mapper {

    fun List<GetStoryResponse.DataStory>.mapToEntities(): List<StoryEntity> {
        val listStory = ArrayList<StoryEntity>()
        for (it in this) {
            val story = StoryEntity(
                id = it.id,
                name = it.name,
                description = it.description,
                photoUrl = it.photoUrl,
                lat = it.lat,
                lon = it.lon
            )
            listStory.add(story)
        }
        return listStory
    }
}