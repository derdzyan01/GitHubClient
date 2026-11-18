package com.example.projecttask.domain.usecase

import com.example.projecttask.domain.model.User
import com.example.projecttask.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(
        since: Int,
        perPage: Int = 30
    ): Flow<List<User>> {
        return repository.getUsers(since, perPage)
    }
}