package com.example.deerdiary.ui.homeScreen

import androidx.lifecycle.LifecycleOwner

sealed class HomeEvent {
    data class ListStory( val lifecycleOwner: LifecycleOwner) : HomeEvent()
}