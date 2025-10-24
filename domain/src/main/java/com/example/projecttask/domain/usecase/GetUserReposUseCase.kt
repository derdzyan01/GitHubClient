package com.example.projecttask.domain.usecase

import com.example.projecttask.domain.model.Repo
import com.example.projecttask.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetUserReposUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(username: String): Single<List<Repo>> {
        return repository.getUserRepos(username)
    }
}