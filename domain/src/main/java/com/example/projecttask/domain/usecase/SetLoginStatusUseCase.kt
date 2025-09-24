package com.example.projecttask.domain.usecase

import com.example.projecttask.domain.repository.AuthRepository
import javax.inject.Inject

class SetLoginStatusUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(isLoggedIn: Boolean) {
        repository.setLoggedIn(isLoggedIn)
    }
}