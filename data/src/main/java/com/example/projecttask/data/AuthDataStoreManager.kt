package com.example.projecttask.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataStoreManager @Inject constructor(
    private val context: Context
) {
    companion object {
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val AVATAR_URI_KEY = stringPreferencesKey("avatar_uri")
    }

    suspend fun saveCredentials(username: String, password: String) {
        context.authDataStore.edit { prefs ->
            prefs[USERNAME_KEY] = username
            prefs[PASSWORD_KEY] = password
        }
    }

    fun getCredentials(): Flow<Pair<String?, String?>> = context.authDataStore.data.map { prefs ->
        Pair(prefs[USERNAME_KEY], prefs[PASSWORD_KEY])
    }

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.authDataStore.edit { prefs ->
            prefs[LOGGED_IN] = isLoggedIn
        }
    }

    fun isLoggedIn(): Flow<Boolean> = context.authDataStore.data.map { prefs ->
        prefs[LOGGED_IN] ?: false
    }

    suspend fun clearCredentials() {
        context.authDataStore.edit { prefs ->
            prefs.remove(USERNAME_KEY)
            prefs.remove(PASSWORD_KEY)
            prefs.remove(AVATAR_URI_KEY)
        }
    }

    suspend fun saveAvatarUri(uri: String) {
        context.authDataStore.edit { prefs ->
            prefs[AVATAR_URI_KEY] = uri
        }
    }

    fun getAvatarUri(): Flow<String?> = context.authDataStore.data.map { prefs ->
        prefs[AVATAR_URI_KEY]
    }
}