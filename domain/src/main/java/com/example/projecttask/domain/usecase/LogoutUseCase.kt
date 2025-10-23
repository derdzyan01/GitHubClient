package com.example.projecttask.domain.usecase

import com.example.projecttask.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        repository.clearCredentials()
        repository.setLoggedIn(false)
    }
}
