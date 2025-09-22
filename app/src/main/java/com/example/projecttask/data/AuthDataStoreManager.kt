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

val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

@Singleton
class AuthDataStoreManager @Inject constructor(
    private val context: Context
) {
    private val LOGGED_IN = booleanPreferencesKey("is_logged_in")

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.authDataStore.edit { prefs ->
            prefs[LOGGED_IN] = isLoggedIn
        }
    }

    fun isLoggedIn(): Flow<Boolean> = context.authDataStore.data.map { prefs ->
        prefs[LOGGED_IN] ?: false
    }
}