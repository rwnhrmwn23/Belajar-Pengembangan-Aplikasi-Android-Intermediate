package com.onedev.storyapp.core.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class GetStoryResponse(
    val error: Boolean,
    val listStory: List<DataStory>,
    val message: String
) {
    @Parcelize
    data class DataStory(
        val createdAt: String,
        val description: String,
        val id: String,
        val lat: Double? = null,
        val lon: Double? = null,
        val name: String,
        val photoUrl: String
    ): Parcelable
}