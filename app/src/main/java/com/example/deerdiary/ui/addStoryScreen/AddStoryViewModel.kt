package com.example.deerdiary.ui.addStoryScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.deerdiary.data.datasource.PostStoryResponse
import com.example.deerdiary.data.repository.Repository
import com.example.deerdiary.data.repository.Resource
import java.io.File

class AddStoryViewModel(
    var repository: Repository,
) : ViewModel() {
    suspend fun uploadStory(
        photo: File,
        description: String,
    ): LiveData<Resource<PostStoryResponse>> {
        return repository.uploadStory(
            photo,
            description,
        )
    }
}
