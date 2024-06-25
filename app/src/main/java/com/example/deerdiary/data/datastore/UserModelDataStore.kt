package com.example.deerdiary.data.datastore

data class UserModelDataStore(
    val token: String,
    val userId: String,
    val nama: String,
    val isLogin: Boolean = false,
)
