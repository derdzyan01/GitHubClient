package com.example.projecttask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.example.projecttask.domain.usecase.LoginUseCase
import com.example.projecttask.domain.usecase.SetLoginStatusUseCase
import com.example.projecttask.presentation.state.LoginUiState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val setLoginStatusUseCase: SetLoginStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private var disposable: Disposable? = null

    fun authenticate(username: String, password: String) {
        _uiState.value = LoginUiState.Loading

        disposable = loginUseCase(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    viewModelScope.launch {
                        setLoginStatusUseCase(true)
                        _uiState.value = LoginUiState.Success(user)
                    }
                },
                { error ->
                    _uiState.value = LoginUiState.Error(error.message ?: "Unknown error")
                }
            )
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}
