package com.example.projecttask.feature_home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttask.domain.model.Repo
import com.example.projecttask.domain.usecase.GetReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getReposUseCase: GetReposUseCase
) : ViewModel() {

    private val _repos = MutableLiveData<List<Repo>>(emptyList())
    val repos: LiveData<List<Repo>> = _repos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val compositeDisposable = CompositeDisposable()

    private var currentPage = 1
    private var isLastPage = false
    private var isCurrentlyLoading = false

    init {
        loadRepos(isInitialLoad = true)
    }

    fun loadMoreRepos() {
        if (isCurrentlyLoading || isLastPage) return
        loadRepos(isInitialLoad = false)
    }

    private fun loadRepos(isInitialLoad: Boolean) {
        if (isInitialLoad) {
            currentPage = 1
            isLastPage = false
            _repos.value = emptyList()
        }

        isCurrentlyLoading = true
        _isLoading.value = true

        val disposable = getReposUseCase(page = currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ newRepos ->
                _isLoading.value = false
                isCurrentlyLoading = false

                if (newRepos.isEmpty()) {
                    isLastPage = true
                } else {
                    val currentList = _repos.value ?: emptyList()
                    _repos.value = currentList + newRepos
                    currentPage++
                }
            }, { throwable ->
                _isLoading.value = false
                isCurrentlyLoading = false
                _error.value = throwable.message ?: "error"
            })

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}