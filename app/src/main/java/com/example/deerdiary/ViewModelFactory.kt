package com.example.deerdiary

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.deerdiary.data.repository.Repository
import com.example.deerdiary.di.Injection
import com.example.deerdiary.ui.addStoryScreen.AddStoryViewModel
import com.example.deerdiary.ui.detailScreen.DetailViewModel
import com.example.deerdiary.ui.homeScreen.HomeViewModel
import com.example.deerdiary.ui.loginScreen.LoginViewModel
import com.example.deerdiary.ui.registerScreen.RegisterViewModel

class ViewModelFactory private constructor(
    private val repository: Repository,
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context) = ViewModelFactory(Injection.provideRepository(context))

        fun clearInstance() {
            INSTANCE = null
        }
    }
}