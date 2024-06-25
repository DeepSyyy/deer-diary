package com.example.deerdiary.ui.settingScreen

import androidx.lifecycle.ViewModel
import com.example.deerdiary.data.repository.Repository

class SettingViewModel(val repository: Repository) : ViewModel() {
    suspend fun deleteSession() {
        repository.deleteSession()
    }
}
