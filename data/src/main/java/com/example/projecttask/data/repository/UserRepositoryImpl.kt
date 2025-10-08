package com.example.projecttask.data.repository

import com.example.projecttask.data.api.GitHubApiService
import com.example.projecttask.data.local.UserDao
import com.example.projecttask.data.mapper.toDomain
import com.example.projecttask.data.mapper.toEntity
import com.example.projecttask.domain.model.User
import com.example.projecttask.domain.repository.UserRepository
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
}