package com.example.projecttask.feature_main.presentation.users.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttask.domain.model.User
import com.example.projecttask.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
): ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error.asSharedFlow()



    private var currentSince = 0
    private var isLastPage = false
    private var isCurrentlyLoading = false


    init {
        loadUsers(
            isRefresh = false
        )
    }

    fun loadMoreUsers() {
        if (isCurrentlyLoading || isLastPage) {
            return
        }
        loadUsers(
            isRefresh = false

        )
    }

    fun refreshUsers() {
        loadUsers(
            isRefresh = true
        )
    }

    private fun loadUsers(isRefresh: Boolean) {
        if (isRefresh) {
            currentSince = 0
            isLastPage = false
            _users.value = emptyList()
        }

        viewModelScope.launch {
            getUsersUseCase(
                since = currentSince
            ).onStart {
                isCurrentlyLoading = true
                _isLoading.value = true
            }.catch { exception ->
                _error.emit(exception.message ?: "an unknown error")
            }.collect { newUsers ->
                if (newUsers.isEmpty()) {
                    isLastPage = true
                } else {
                    _users.value = if (isRefresh) newUsers else _users.value + newUsers
                    currentSince = newUsers.last().id
                }
            }
            isCurrentlyLoading = false
            _isLoading.value = false
        }
    }
}