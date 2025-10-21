package com.example.projecttask.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

enum class ThemeMode(val value: Int) {
    LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
    DARK(AppCompatDelegate.MODE_NIGHT_YES),
    SYSTEM(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

    companion object {
        fun fromValue(value: Int): ThemeMode {
            return values().find { it.value == value } ?: SYSTEM
        }
    }
}

@Singleton
class ThemeManager @Inject constructor(
    private val context: Context
) {
    companion object {
        private val THEME_KEY = intPreferencesKey("theme_mode")
    }

    suspend fun setTheme(themeMode: ThemeMode) {
        context.authDataStore.edit { prefs ->
            prefs[THEME_KEY] = themeMode.value
        }
        AppCompatDelegate.setDefaultNightMode(themeMode.value)
    }

    fun getTheme(): Flow<ThemeMode> = context.authDataStore.data.map { prefs ->
        ThemeMode.fromValue(prefs[THEME_KEY] ?: ThemeMode.SYSTEM.value)
    }

    suspend fun applyTheme() {
        getTheme().collect { themeMode ->
            AppCompatDelegate.setDefaultNightMode(themeMode.value)
        }
    }
}