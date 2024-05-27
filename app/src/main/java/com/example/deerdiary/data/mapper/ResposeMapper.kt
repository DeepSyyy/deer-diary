package com.example.deerdiary.data.mapper

import com.example.deerdiary.data.datasource.StoryResponse
import com.example.deerdiary.ui.homeScreen.model.StoryModel

fun StoryResponse.toStoryModel() = StoryModel(
    name = story?.name ?: "",
    description = story?.description ?: "",
    lon = story?.lon ?: Any(),
    id = story?.id ?: "",
    lat = story?.lat ?: Any(),
    photoUrl = story?.photoUrl ?: "",
    createdAt = story?.createdAt ?: ""
)