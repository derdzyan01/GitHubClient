package com.example.projecttask.feature_main.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttask.data.ThemeManager
import com.example.projecttask.data.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val themeManager: ThemeManager
) : ViewModel() {

    private val _currentTheme = MutableStateFlow(ThemeMode.SYSTEM)
    val currentTheme = _currentTheme.asStateFlow()

    init {
        loadCurrentTheme()
    }

    private fun loadCurrentTheme() {
        themeManager.getTheme()
            .onEach { theme ->
                _currentTheme.value = theme
            }
            .launchIn(viewModelScope)
    }

    fun setTheme(themeMode: ThemeMode) {
        viewModelScope.launch {
            themeManager.setTheme(themeMode)
        }
    }
}