package com.example.projecttask.presentation.viewmodel

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.projecttask.domain.repository.AuthRepository
import com.example.projecttask.data.repository.AuthRepositoryImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import androidx.core.content.edit

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository: AuthRepository = AuthRepositoryImpl()
    val loginResult = MutableLiveData<Boolean>()
    private val compositeDisposable = CompositeDisposable()

    fun authenticate(username: String, password: String) {
        val disposable = authRepository.authenticate(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _ ->
                    loginResult.value = true
                    getApplication<Application>().getSharedPreferences("auth_prefs", MODE_PRIVATE)
                        .edit {
                            putBoolean("is_logged_in", true)
                        }
                },
                { _ -> loginResult.value = false }
            )

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}