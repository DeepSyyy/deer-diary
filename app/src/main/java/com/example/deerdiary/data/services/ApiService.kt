package com.example.deerdiary.data.services

import com.example.deerdiary.data.datasource.LoginResponse
import com.example.deerdiary.data.datasource.PostStoryResponse
import com.example.deerdiary.data.datasource.RegisterResponse
import com.example.deerdiary.data.datasource.StoriesResponse
import com.example.deerdiary.data.datasource.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Melakukan request GET ke endpoint /stories untuk mendapatkan semua cerita
    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20,
        @Query("location") location: String? = null,
    ): StoriesResponse

    // Melakukan request GET ke endpoint /stories/{id} untuk mendapatkan cerita berdasarkan id
    @GET("stories/{id}")
    suspend fun getStory(
        @Path("id") id: String,
    ): StoryResponse

    // Melakukan request POST ke endpoint /login untuk login

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    // Melakukan request POST ke endpoint /register untuk register
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): RegisterResponse

    // Melakukan request POST ke endpoint /stories untuk membuat cerita baru
    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): PostStoryResponse
}
