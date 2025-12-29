package com.example.mixandmealapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.mixandmealapp.data.keys.TokenKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TokenRepository(
    private val dataStore: DataStore<Preferences>
) {

    val tokenValue: Flow<String?> = dataStore.data
        .map {prefs -> prefs[TokenKeys.TOKEN_VALUE]}

    suspend fun getToken(): String? =
        dataStore.data.first()[TokenKeys.TOKEN_VALUE]

    suspend fun getTokenOrDefault(default: String = ""): String =
        dataStore.data.map { it[TokenKeys.TOKEN_VALUE] ?: default }
            .first()

    suspend fun setToken(tokenValue: String?){
        dataStore.edit {prefs ->
            if(tokenValue == null) {
                prefs.remove(TokenKeys.TOKEN_VALUE)
            } else{
                prefs[TokenKeys.TOKEN_VALUE] = tokenValue
            }
        }
    }

    suspend fun clearToken() {
        dataStore.edit { prefs ->
            prefs.remove(TokenKeys.TOKEN_VALUE)
        }
    }

}