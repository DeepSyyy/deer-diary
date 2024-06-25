package com.example.deerdiary.ui.detailScreen

import androidx.lifecycle.LifecycleOwner

sealed class DetailEvent {
    data class GetStory(val id: String, val lifecycleOwner: LifecycleOwner) : DetailEvent()
}
