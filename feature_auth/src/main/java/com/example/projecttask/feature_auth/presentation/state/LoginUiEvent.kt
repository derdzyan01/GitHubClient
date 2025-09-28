package com.example.projecttask.feature_auth.presentation.state

internal sealed interface LoginUiEvent {
    data class ShowError(val message: String) : LoginUiEvent
    object NavigateToHome : LoginUiEvent
}