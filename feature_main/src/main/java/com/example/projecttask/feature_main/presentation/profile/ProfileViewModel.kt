package com.example.projecttask.feature_main.presentation.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttask.domain.model.User
import com.example.projecttask.domain.repository.AuthRepository
import com.example.projecttask.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


// event-nery petqa tanem arancin file-eri mej???

internal sealed interface ProfileUiEvent {
    object LogoutSuccess : ProfileUiEvent
}




internal data class ProfileUiState(
    val user: User? = null,
    val avatarUri: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ProfileUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val disposables = CompositeDisposable()

    init {
        loadUserProfile()
        observeAvatar()
    }

    private fun loadUserProfile() {
        val disposable = authRepository.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _uiState.value = _uiState.value.copy(isLoading = true) }
            .doOnTerminate { _uiState.value = _uiState.value.copy(isLoading = false) }
            .subscribe(
                { user ->
                    _uiState.value = _uiState.value.copy(user = user)
                },
                { error ->
                    _uiState.value = _uiState.value.copy(error = error.message ?: "An unknown error occurred")
                }
            )
        disposables.add(disposable)
    }

    private fun observeAvatar() {
        authRepository.getAvatarUri()
            .onEach { uri ->
                _uiState.value = _uiState.value.copy(avatarUri = uri)
            }
            .launchIn(viewModelScope)
    }

    fun onAvatarSelected(uri: Uri) {
        viewModelScope.launch {
            authRepository.saveAvatarUri(uri.toString())
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            logoutUseCase()
            _uiEvent.emit(ProfileUiEvent.LogoutSuccess)
        }
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}