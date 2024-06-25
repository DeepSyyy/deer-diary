package com.example.deerdiary.di

import android.content.Context
import com.example.deerdiary.data.dao.StoryDatabase
import com.example.deerdiary.data.datastore.DataStoreToken
import com.example.deerdiary.data.datastore.dataStore
import com.example.deerdiary.data.repository.Repository
import com.example.deerdiary.data.services.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = DataStoreToken.getInstance(context.dataStore)
        val user = pref.getToken(context)
        val apiService = ApiConfig.getApiService(runBlocking { user.first() })
        val storyDatabase = StoryDatabase.getDatabase(context)
        return Repository.getInstance(apiService, pref, storyDatabase)
    }
}
