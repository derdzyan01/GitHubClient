package com.example.projecttask.domain.usecase

import com.example.projecttask.domain.model.Repo
import com.example.projecttask.domain.repository.RepoRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetReposUseCase @Inject constructor(
    private val repository: RepoRepository
) {
    operator fun invoke(page: Int, perPage: Int = 20): Single<List<Repo>> {
        return repository.getRepos(page, perPage)
    }
}