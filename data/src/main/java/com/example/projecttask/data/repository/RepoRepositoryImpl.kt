package com.example.projecttask.data.repository

import com.example.projecttask.data.api.GitHubApiService
import com.example.projecttask.data.local.RepoDao
import com.example.projecttask.data.mapper.toDomain
import com.example.projecttask.data.mapper.toEntity
import com.example.projecttask.domain.model.Repo
import com.example.projecttask.domain.repository.RepoRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class RepoRepositoryImpl @Inject constructor(
    private val apiService: GitHubApiService,
    private val repoDao: RepoDao
) : RepoRepository {

    override fun getRepos(page: Int, perPage: Int): Single<List<Repo>> {
        return apiService.getRepos(page, perPage)
            .flatMap { reposDto ->
                val repoEntities = reposDto.map { it.toEntity() }
                val clearCacheCompletable = if (page == 1) repoDao.clearAll() else Completable.complete()

                clearCacheCompletable
                    .andThen(repoDao.insertAll(repoEntities))
                    .toSingleDefault(repoEntities.map { it.toDomain() })
            }
            .onErrorResumeNext { throwable ->
                val offset = (page - 1) * perPage
                repoDao.getRepos(limit = perPage, offset = offset)
                    .flatMap { entities ->
                        if (entities.isNotEmpty()) {
                            Single.just(entities.map { it.toDomain() })
                        } else {
                            Single.error(throwable)
                        }
                    }
            }
    }
}