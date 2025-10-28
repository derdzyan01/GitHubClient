package com.example.projecttask.feature_main.presentation.userdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.projecttask.domain.model.Repo
import com.example.projecttask.domain.usecase.GetUserReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val getUserReposUseCase: GetUserReposUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_USERNAME = "username"
        private const val KEY_AVATAR_URL = "avatarUrl"
    }

    private val _repositories = MutableLiveData<List<Repo>>(emptyList())
    val repositories: LiveData<List<Repo>> = _repositories

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val compositeDisposable = CompositeDisposable()

    val username: String? = savedStateHandle.get<String>(KEY_USERNAME)
    val avatarUrl: String? = savedStateHandle.get<String>(KEY_AVATAR_URL)

    init {
        username?.let { loadUserRepositories(it) }
    }

    private fun loadUserRepositories(username: String) {
        _isLoading.value = true
        val disposable = getUserReposUseCase(username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { repos ->
                    _isLoading.value = false
                    _repositories.value = repos
                },
                { throwable ->
                    _isLoading.value = false
                    _error.value = throwable.message ?: "failed to load repos"
                }
            )
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}