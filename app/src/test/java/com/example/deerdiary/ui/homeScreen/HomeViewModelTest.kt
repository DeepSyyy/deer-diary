package com.example.deerdiary.ui.homeScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.deerdiary.MainDispatcherRule
import com.example.deerdiary.adapter.ListStoryAdapter
import com.example.deerdiary.data.datasource.Story
import com.example.deerdiary.data.repository.Repository
import com.example.deerdiary.utils.DummyData
import com.example.deerdiary.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: Repository

    @Test
    fun `when Get Stories Should Not Null and Return Data`() =
        runTest {
            val dummyStory = DummyData.generateDummyStoriesResponse()
            val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStory)
            val expectedStories = MutableLiveData<PagingData<Story>>()
            expectedStories.value = data
            Mockito.`when`(repository.getPagginationStories()).thenReturn(expectedStories)

            val homeViewModel = HomeViewModel(repository)
            val actualStory: PagingData<Story> = homeViewModel.stories.getOrAwaitValue()

            val differ =
                AsyncPagingDataDiffer(
                    diffCallback = ListStoryAdapter.DIFF_CALLBACK,
                    updateCallback = noopListUpdateCallback,
                    workerDispatcher = Dispatchers.Main,
                )
            differ.submitData(actualStory)

            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(dummyStory.size, differ.snapshot().size)
            Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
        }

    @SuppressLint("CheckResult")
    @Test
    fun `when Get Stories Should Not Null and Return No Data`() =
        runTest {
            val data: PagingData<Story> = PagingData.from(emptyList())
            val expectedStories = MutableLiveData<PagingData<Story>>()
            expectedStories.value = data
            Mockito.mockStatic(Log::class.java)
            Mockito.`when`(repository.getPagginationStories()).thenReturn(expectedStories)
            val mainViewModel = HomeViewModel(repository)
            val actualStories: PagingData<Story> = mainViewModel.stories.getOrAwaitValue()
            val differ =
                AsyncPagingDataDiffer(
                    diffCallback = ListStoryAdapter.DIFF_CALLBACK,
                    updateCallback = noopListUpdateCallback,
                    workerDispatcher = Dispatchers.Main,
                )
            differ.submitData(actualStories)
            Assert.assertEquals(0, differ.snapshot().size)
        }

    class StoryPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
        companion object {
            fun snapshot(items: List<Story>): PagingData<Story> {
                return PagingData.from(items)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }
}

val noopListUpdateCallback =
    object : ListUpdateCallback {
        override fun onInserted(
            position: Int,
            count: Int,
        ) {
        }

        override fun onRemoved(
            position: Int,
            count: Int,
        ) {
        }

        override fun onMoved(
            fromPosition: Int,
            toPosition: Int,
        ) {
        }

        override fun onChanged(
            position: Int,
            count: Int,
            payload: Any?,
        ) {
        }
    }
