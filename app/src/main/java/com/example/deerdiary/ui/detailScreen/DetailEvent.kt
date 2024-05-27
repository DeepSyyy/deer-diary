package com.example.deerdiary.ui.detailScreen

import androidx.lifecycle.LifecycleOwner

sealed class DetailEvent {
    data class getStory(val id: String, val lifecycleOwner: LifecycleOwner) : DetailEvent()
}