package com.example.projecttask.domain.repository

import io.reactivex.rxjava3.core.Observable
import com.example.projecttask.data.model.User

interface AuthRepository {
    fun authenticate(username: String, password: String): Observable<User>
}