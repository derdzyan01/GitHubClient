package com.example.projecttask.domain.usecase

import io.reactivex.rxjava3.core.Single
import com.example.projecttask.domain.model.User
import com.example.projecttask.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(username: String, password: String): Single<User> =
        repository.authenticate(username, password)
}