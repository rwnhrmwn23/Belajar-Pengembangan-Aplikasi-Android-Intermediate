package com.onedev.storyapp.core.data.source.local

import com.onedev.storyapp.core.data.source.local.entity.RemoteKeysEntity
import com.onedev.storyapp.core.data.source.local.entity.StoryEntity
import com.onedev.storyapp.core.data.source.local.room.RemoteKeysDao
import com.onedev.storyapp.core.data.source.local.room.StoryDao

class LocalDataSource(private val storyDao: StoryDao, private val remoteKeysDao: RemoteKeysDao) {

    fun getAllStory() = storyDao.getAllStory()

    suspend fun insertStory(story: List<StoryEntity>) = storyDao.insertStory(story)

    suspend fun deleteAll() = storyDao.deleteAll()

    suspend fun insertAll(remoteKey: List<RemoteKeysEntity>) = remoteKeysDao.insertAll(remoteKey)

    suspend fun getRemoteKeysId(id: String) = remoteKeysDao.getRemoteKeysId(id)

    suspend fun deleteRemoteKeys() = remoteKeysDao.deleteRemoteKeys()

}