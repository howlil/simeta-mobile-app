package com.dev.simeta.helpers

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Declare the delegate at the top level
private val Context.dataStore by preferencesDataStore(name = "auth_preferences")

object AuthPreferences {
    private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")

    // Save token
    suspend fun saveAuthToken(context: Context, token: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = token
        }
    }

    // Read token
    fun getAuthToken(context: Context): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[AUTH_TOKEN_KEY]
        }
    }

    // Clear token (logout)
    suspend fun clearAuthToken(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN_KEY)
        }
    }
}
