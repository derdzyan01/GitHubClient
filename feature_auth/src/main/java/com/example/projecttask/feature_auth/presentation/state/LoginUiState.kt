package com.example.projecttask.feature_auth.presentation.state

import com.example.projecttask.domain.model.User

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    data class Success(val user: User) : LoginUiState
}