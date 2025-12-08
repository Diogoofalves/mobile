package com.tpbcontrol.mobile.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "tpb_control_prefs")

class TokenStore(private val context: Context) {
    private val tokenKey = stringPreferencesKey("auth_token")

    val tokenFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[tokenKey]
    }

    suspend fun saveToken(token: String?) {
        context.dataStore.edit { prefs ->
            if (token.isNullOrBlank()) {
                prefs.remove(tokenKey)
            } else {
                prefs[tokenKey] = token
            }
        }
    }

    suspend fun currentToken(): String? = context.dataStore.data.first()[tokenKey]
}
