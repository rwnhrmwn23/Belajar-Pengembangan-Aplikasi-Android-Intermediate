package com.onedev.storyapp.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.onedev.storyapp.core.data.source.local.entity.RemoteKeysEntity
import com.onedev.storyapp.core.data.source.local.entity.StoryEntity

@Database(
    entities = [StoryEntity::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}