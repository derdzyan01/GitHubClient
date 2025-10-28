package com.example.projecttask.data.repository

import com.example.projecttask.data.api.GitHubApiService
import com.example.projecttask.data.local.UserDao
import com.example.projecttask.data.mapper.toDomain
import com.example.projecttask.data.mapper.toEntity
import com.example.projecttask.domain.model.Repo
import com.example.projecttask.domain.model.User
import com.example.projecttask.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val apiService: GitHubApiService,
    private val userDao: UserDao
): UserRepository {
    override fun getUsers(
        since: Int,
        perPage: Int
    ) : Flow<List<User>> = flow {
        try {
            val remoteUsers = apiService.getUsers(
                since = since,
                perPage = perPage
            )

            val usersEntities = remoteUsers.map { it.toEntity() }

            if (since == 0) {
                userDao.clearAll()
            }

            userDao.insertAll(usersEntities)
            emit(usersEntities.map { it.toDomain() })
        } catch (e: Exception) {
            val offset = since
            val cachedUsers = userDao.getUsers(
                limit = perPage,
                offset = offset
            )
            if (cachedUsers.isNotEmpty()) {
                emit(cachedUsers.map { it.toDomain() })
            } else {
                throw e
            }
        }
    }

    override fun getUserRepos(username: String): Single<List<Repo>> {
        return apiService.getUserRepos(username).map { dtos ->
            dtos.map { dto ->
                Repo(
                    id = dto.id,
                    name = dto.name,
                    description = dto.description,
                    stars = dto.stars,
                    language = dto.language
                )
            }
        }
    }
}