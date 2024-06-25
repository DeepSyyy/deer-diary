package com.example.deerdiary.ui.registerScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.deerdiary.data.datasource.RegisterResponse
import com.example.deerdiary.data.repository.Repository
import com.example.deerdiary.data.repository.Resource

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    suspend fun register(
        username: String,
        password: String,
        email: String,
    ): LiveData<Resource<RegisterResponse>> {
        return repository.register(username, password, email)
    }
}
