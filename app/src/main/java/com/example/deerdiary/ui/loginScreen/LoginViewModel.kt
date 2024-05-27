package com.example.deerdiary.ui.loginScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.deerdiary.data.datasource.LoginResponse
import com.example.deerdiary.data.datastore.UserModelDataStore
import com.example.deerdiary.data.repository.Repository
import com.example.deerdiary.data.repository.Resource

class LoginViewModel(private val repository: Repository): ViewModel() {

    suspend fun login(username: String, password: String): LiveData<Resource<LoginResponse>> {
        return repository.login(username, password)
    }

    suspend fun saveSession(userModelDataStore: UserModelDataStore) {
        repository.saveSession(userModelDataStore)
    }
}