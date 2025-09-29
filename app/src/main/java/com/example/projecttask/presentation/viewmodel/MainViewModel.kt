package com.example.projecttask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttask.domain.usecase.GetLoginStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

internal sealed interface NavigationEvent {
    object ToLoginScreen : NavigationEvent
    object ToHomeScreen : NavigationEvent
}

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val getLoginStatusUseCase: GetLoginStatusUseCase
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun decideInitialScreen() {
        viewModelScope.launch {
            val isLoggedIn = getLoginStatusUseCase().first()
            if (isLoggedIn) {
                _navigationEvent.emit(NavigationEvent.ToHomeScreen)
            } else {
                _navigationEvent.emit(NavigationEvent.ToLoginScreen)
            }
        }
    }
}