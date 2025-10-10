package com.example.projecttask.domain.repository

import com.example.projecttask.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(since: Int, perPage: Int): Flow<List<User>>
}