package com.example.projecttask.domain.repository

import io.reactivex.rxjava3.core.Single
import com.example.projecttask.domain.model.User

interface AuthRepository {
    fun authenticate(username: String, password: String): Single<User>
}