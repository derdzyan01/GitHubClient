package com.example.projecttask.domain.repository

import com.example.projecttask.domain.model.User
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun authenticate(username: String, password: String): Single<User>
    suspend fun setLoggedIn(isLoggedIn: Boolean)
    fun isLoggedIn(): Flow<Boolean>
}