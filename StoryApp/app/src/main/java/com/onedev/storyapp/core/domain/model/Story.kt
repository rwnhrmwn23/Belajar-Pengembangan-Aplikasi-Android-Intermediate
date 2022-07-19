package com.onedev.storyapp.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val lat: Double? = 0.0,
    val lon: Double? = 0.0
): Parcelable