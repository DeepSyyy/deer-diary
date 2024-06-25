package com.example.deerdiary.data.datasource

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoryResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,
    @field:SerializedName("message")
    val message: String? = null,
    @field:SerializedName("story")
    val story: Story? = null,
)

@Entity(tableName = "stories")
@Parcelize
data class Story(
    @PrimaryKey
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,
    @field:SerializedName("createdAt")
    val createdAt: String? = null,
    @field:SerializedName("name")
    val name: String? = null,
    @field:SerializedName("description")
    val description: String? = null,
    @field:SerializedName("lon")
    val lon: Double? = null,
    @field:SerializedName("lat")
    val lat: Double? = null,
) : Parcelable
