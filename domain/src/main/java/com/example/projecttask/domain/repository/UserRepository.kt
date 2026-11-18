package com.example.projecttask.domain.repository

import com.example.projecttask.domain.model.Repo
import com.example.projecttask.domain.model.User
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(since: Int, perPage: Int): Flow<List<User>>
    fun getUserRepos(username: String): Single<List<Repo>>
}