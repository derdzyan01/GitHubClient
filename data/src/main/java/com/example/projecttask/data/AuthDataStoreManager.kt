package com.example.projecttask.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import androidx.datastore.preferences.core.stringPreferencesKey

val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")
private val USERNAME_KEY = stringPreferencesKey("username")
private val PASSWORD_KEY = stringPreferencesKey("password")

@Singleton
internal class AuthDataStoreManager @Inject constructor(
    private val context: Context
) {
    private val LOGGED_IN = booleanPreferencesKey("is_logged_in")

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
}