package com.onedev.storyapp.ui.fragment.story

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.android.gms.common.api.Api
import com.onedev.storyapp.core.data.source.remote.RemoteDataSource
import com.onedev.storyapp.core.data.source.remote.network.ApiResponse
import com.onedev.storyapp.core.data.source.remote.response.Story

class StoryPagingSource(private val remote: RemoteDataSource) :
    PagingSource<Int, Story.GetResponse.DataStory>() {

    override fun getRefreshKey(state: PagingState<Int, Story.GetResponse.DataStory>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story.GetResponse.DataStory> {
        var data: Story.GetResponse? = null

        return try {
            val position = params.key ?: PAGE_INDEX
            val responseData = remote.story(position, params.loadSize, 0)

            responseData.collect {
                data = when (it) {
                    is ApiResponse.Success -> {
                        it.data
                    }
                    is ApiResponse.Error -> {
                        null
                    }
                    else -> {
                        null
                    }
                }
            }

            LoadResult.Page(
                data = data?.listStory as List,
                prevKey = if (position == PAGE_INDEX) null else position - 1,
                nextKey = if (data?.listStory?.isEmpty() == true) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private companion object {
        const val PAGE_INDEX = 1
    }
}