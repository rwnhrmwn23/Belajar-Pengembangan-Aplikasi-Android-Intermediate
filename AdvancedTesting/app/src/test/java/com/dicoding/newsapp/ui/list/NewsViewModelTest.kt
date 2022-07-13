package com.dicoding.newsapp.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.newsapp.utils.DataDummy
import com.dicoding.newsapp.data.NewsRepository
import com.dicoding.newsapp.data.Result
import com.dicoding.newsapp.data.local.entity.NewsEntity
import com.dicoding.newsapp.utils.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NewsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var newsRepository: NewsRepository
    private lateinit var newsViewModel: NewsViewModel
    private val dummyNews = DataDummy.generateDummyNewsEntity()

    @Before
    fun setUp() {
        newsViewModel = NewsViewModel(newsRepository)
    }

//    @Test
//    fun `when get HeadlineNews should not null and return success`() {
//        val observer = Observer<Result<List<NewsEntity>>> {}
//        try {
//            val expectedNews = MutableLiveData<Result<List<NewsEntity>>>()
//            expectedNews.value = Result.Success(dummyNews)
//            `when`(newsRepository.getHeadlineNews()).thenReturn(expectedNews)
//
//            val actualNews = newsViewModel.getHeadlineNews().observeForever(observer)
//
//            Mockito.verify(newsRepository).getHeadlineNews()
//            Assert.assertNotNull(actualNews)
//
//        } finally {
//            newsViewModel.getHeadlineNews().removeObserver(observer)
//        }
//    }

    @Test
    fun `when get HeadlineNews should not null and return success`() {
        val expectedNews = MutableLiveData<Result<List<NewsEntity>>>()
        expectedNews.value = Result.Success(dummyNews)
        `when`(newsRepository.getHeadlineNews()).thenReturn(expectedNews)

        val actualNews = newsViewModel.getHeadlineNews().getOrAwaitValue()

        Mockito.verify(newsRepository).getHeadlineNews()
        assertNotNull(actualNews)
        assertTrue(actualNews is Result.Success)
        assertEquals(dummyNews.size, (actualNews as Result.Success).data.size)
    }

    @Test
    fun `when get HeadlineNews Network Error should return error`() {
        val headlinesNews = MutableLiveData<Result<List<NewsEntity>>>()
        headlinesNews.value = Result.Error("Error")
        `when`(newsRepository.getHeadlineNews()).thenReturn(headlinesNews)

        val actualNews = newsViewModel.getHeadlineNews().getOrAwaitValue()

        Mockito.verify(newsRepository).getHeadlineNews()
        assertNotNull(actualNews)
        assertTrue(actualNews is Result.Error)
    }

    @Test
    fun `when get BookmarkedNews should not run and return success`() {
        val expectedBookmarkedNews = MutableLiveData<List<NewsEntity>>()
        expectedBookmarkedNews.value = dummyNews
        `when`(newsRepository.getBookmarkedNews()).thenReturn(expectedBookmarkedNews)

        val actualNews = newsViewModel.getBookmarkedNews().getOrAwaitValue()

        Mockito.verify(newsRepository).getBookmarkedNews()
        assertNotNull(actualNews)
        assertEquals(dummyNews.size, actualNews.size)
    }
}