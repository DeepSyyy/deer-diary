package com.example.deerdiary.data.datasource

import com.google.gson.annotations.SerializedName

data class PostStoryResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String,
)
