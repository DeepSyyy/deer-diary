package com.example.deerdiary.ui.mapUi

import androidx.lifecycle.LifecycleOwner

sealed class MapsEvent {
    data class ListStory(val lifecycleOwner: LifecycleOwner) : MapsEvent()
}
