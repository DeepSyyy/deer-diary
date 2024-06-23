package com.example.deerdiary.utils

import com.example.deerdiary.data.datasource.Story

object DummyData {
    fun generateDummyStoriesResponse(): List<Story> {
        val items: MutableList<Story> = mutableListOf()
        for (i in 0..100) {
            items.add(
                Story(
                    id = "story-FvU4u0Vp2S3PMsFg",
                    photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                    createdAt = "2022-01-08T06:34:18.598Z",
                    name = "Dimas",
                    description = "Lorem Ipsum",
                    lon = -16.002,
                    lat = -10.212,
                ),
            )
        }
        return items
    }
}
