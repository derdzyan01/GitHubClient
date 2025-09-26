package com.example.projecttask.feature_auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttask.domain.usecase.LoginUseCase
import com.example.projecttask.domain.usecase.SetLoginStatusUseCase
import com.example.projecttask.feature_auth.presentation.state.LoginUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val setLoginStatusUseCase: SetLoginStatusUseCase
) : ViewModel() {


    private val _uiEvent = MutableSharedFlow<LoginUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var disposable: Disposable? = null

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun authenticate(username: String, password: String) {
        _isLoading.value = true

        disposable = loginUseCase(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    viewModelScope.launch {
                        setLoginStatusUseCase(true)
                        _isLoading.value = false
                        _uiEvent.emit(LoginUiEvent.NavigateToHome)
                    }
                },
                { error ->
                    viewModelScope.launch {
                        _uiEvent.emit(LoginUiEvent.ShowError(error.message ?: "Unknown error"))
                        _isLoading.value = false
                    }
                }
            )
    }


    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}