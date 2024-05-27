package com.example.deerdiary.ui.detailScreen

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deerdiary.data.repository.Repository
import com.example.deerdiary.data.repository.Resource
import com.example.deerdiary.ui.detailScreen.model.StoryModel
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository): ViewModel() {
    private val _story = MutableLiveData<StoryModel>()
    val story: LiveData<StoryModel> = _story

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun processEvent(event: DetailEvent){
        when(event){
            is DetailEvent.getStory -> {
                getStory(event.id, event.lifecycleOwner)
            }
        }
    }

    private fun getStory(id: String, lifecycleOwner: LifecycleOwner) {
        _isLoading.value = true
        _isEmpty.value = false
        viewModelScope.launch {
            repository.getStory(id).observe(lifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _isLoading.value = true
                    }

                    is Resource.Success -> {
                        _isLoading.value = false
                        if (resource.data.story == null) {
                            _isEmpty.value = true
                        } else {
                            _story.value = StoryModel(
                                name = resource.data.story.name ?: "",
                                description = resource.data.story.description ?: "",
                                lon = resource.data.story.lon ?: "",
                                id = resource.data.story.id ?: "",
                                lat = resource.data.story.lat ?: "",
                                photoUrl = resource.data.story.photoUrl ?: "",
                                createdAt = resource.data.story.createdAt ?: ""
                            )
                        }
                    }

                    is Resource.Error -> {
                        _isLoading.value = false
                        Log.e("DetailViewModel", "getStory: ${resource.error}")

                    }
                }
            }
        }
    }
}