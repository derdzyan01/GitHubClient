package com.example.projecttask.domain.usecase

import com.example.projecttask.domain.model.User
import com.example.projecttask.domain.repository.AuthRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Single<User> = repository.getCurrentUser()
}
