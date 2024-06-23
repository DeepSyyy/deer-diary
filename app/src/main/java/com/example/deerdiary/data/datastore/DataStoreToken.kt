package com.example.deerdiary.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension untuk Context
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

class DataStoreToken private constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        @Suppress("ktlint:standard:property-naming")
        @Volatile
        private var INSTANCE: DataStoreToken? = null

        private val NAME_KEY = stringPreferencesKey("nama")
        private val USER_ID_KEY = stringPreferencesKey("userId")
        private val IS_LOGIN_KEY = stringPreferencesKey("isLogin")
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): DataStoreToken {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataStoreToken(dataStore).also { INSTANCE = it }
            }
        }
    }

    suspend fun saveSession(userModelDataStore: UserModelDataStore) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = userModelDataStore.token
            preferences[USER_ID_KEY] = userModelDataStore.userId
            preferences[NAME_KEY] = userModelDataStore.nama
            preferences[IS_LOGIN_KEY] = userModelDataStore.isLogin.toString()
        }
    }

    fun getSession(): Flow<UserModelDataStore> {
        return dataStore.data.map { preferences ->
            UserModelDataStore(
                token = preferences[TOKEN_KEY] ?: "",
                userId = preferences[USER_ID_KEY] ?: "",
                nama = preferences[NAME_KEY] ?: "",
                isLogin = preferences[IS_LOGIN_KEY]?.toBoolean() ?: false,
            )
        }
    }

    fun getToken(context: Context): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[TOKEN_KEY] ?: ""
            }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
