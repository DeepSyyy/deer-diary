package com.example.deerdiary.ui.mapUi

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deerdiary.data.repository.Repository
import com.example.deerdiary.data.repository.Resource
import com.example.deerdiary.ui.homeScreen.model.StoryModel
import kotlinx.coroutines.launch

class MapsViewModel(
    private val repository: Repository,
) : ViewModel() {
    private val _listStory = MutableLiveData<List<StoryModel>>()
    val listStory: LiveData<List<StoryModel>> = _listStory

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun processEvent(event: MapsEvent) {
        when (event) {
            is MapsEvent.ListStory -> {
                getStoriesWithLocation(event.lifecycleOwner)
            }
        }
    }

    private fun getStoriesWithLocation(lifecycleOwner: LifecycleOwner) {
        _isLoading.value = true
        _isEmpty.value = false
        viewModelScope.launch {
            repository.getALlStoriesWithLocation(1).observe(lifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _isLoading.value = true
                    }

                    is Resource.Success -> {
                        _isLoading.value = false
                        if (resource.data.listStory.isEmpty()) {
                            _isEmpty.value = true
                        } else {
                            _listStory.value =
                                resource.data.listStory.map {
                                    StoryModel(
                                        name = it.name ?: "",
                                        description = it.description ?: "",
                                        lon = it.lon ?: "",
                                        id = it.id,
                                        lat = it.lat ?: "",
                                        photoUrl = it.photoUrl ?: "",
                                        createdAt = it.createdAt ?: "",
                                    )
                                }
                        }
                    }

                    is Resource.Error -> {
                        _isLoading.value = false
                        Log.e("HomeViewModel", "onFailure: ${resource.error}")
                    }
                }
            }
        }
    }
}
