package com.example.projecttask.domain.repository

import io.reactivex.rxjava3.core.Single
import com.example.projecttask.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun authenticate(username: String, password: String): Single<User>
    suspend fun setLoggedIn(isLoggedIn: Boolean)
    fun isLoggedIn(): Flow<Boolean>
}