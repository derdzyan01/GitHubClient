package com.example.projecttask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projecttask.domain.usecase.GetLoginStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getLoginStatusUseCase: GetLoginStatusUseCase
) : ViewModel() {

    fun isLoggedIn(): Flow<Boolean> = getLoginStatusUseCase()
}