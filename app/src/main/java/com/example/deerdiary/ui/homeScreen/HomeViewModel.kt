package com.example.deerdiary.ui.homeScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.deerdiary.data.datasource.Story
import com.example.deerdiary.data.repository.Repository

class HomeViewModel(
    var repository: Repository,
) : ViewModel() {
    private val _listStory = MutableLiveData<PagingData<Story>>()
    val listStory: LiveData<PagingData<Story>> = _listStory

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var stories: LiveData<PagingData<Story>> =
        repository.getPagginationStories().cachedIn(viewModelScope)

    fun processEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.ListStory -> {
                stories.observe(event.lifecycleOwner) { pagingData ->
                    _listStory.value = pagingData
                    _isLoading.value = false
                }
            }
        }
    }
}
