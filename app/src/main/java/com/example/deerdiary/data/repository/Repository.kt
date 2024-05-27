package com.example.deerdiary.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.deerdiary.data.datasource.LoginResponse
import com.example.deerdiary.data.datasource.RegisterResponse
import com.example.deerdiary.data.datasource.StoriesResponse
import com.example.deerdiary.data.datasource.StoryResponse
import com.example.deerdiary.data.datastore.DataStoreToken
import com.example.deerdiary.data.datastore.UserModelDataStore
import com.example.deerdiary.data.services.ApiService
import kotlinx.coroutines.flow.Flow

class Repository private constructor(
    private val apiService: ApiService,
    private val dataStoreToken: DataStoreToken
){
    suspend fun login(email: String, password: String) : LiveData<Resource<LoginResponse>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.login(email, password)
            if (response.error == false) {
                emit(Resource.Success(response))
            } else {
                emit(Resource.Error(response.message.toString()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    suspend fun saveSession(userModelDataStore: UserModelDataStore) {
        dataStoreToken.saveSession(userModelDataStore)
    }

    suspend fun getSession(): Flow<UserModelDataStore> {
        return dataStoreToken.getSession()
    }

    suspend fun register(username: String, password: String, email: String): LiveData<Resource<RegisterResponse>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.register(username, password, email)
            if (response.error == false) {
                emit(Resource.Success(response))
            } else {
                emit(Resource.Error(response.message.toString()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    suspend fun getStories(): LiveData<Resource<StoriesResponse>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getAllStories()
            if (response.error == false) {
                emit(Resource.Success(response))
            } else {
                emit(Resource.Error(response.message.toString()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    suspend fun getStory(id: String): LiveData<Resource<StoryResponse>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getStory(id)
            if (response.error == false) {
                emit(Resource.Success(response))
            } else {
                emit(Resource.Error(response.message.toString()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }

    }

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(apiService: ApiService, dataStoreToken: DataStoreToken): Repository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Repository(apiService, dataStoreToken).also { INSTANCE = it }
            }
    }
}