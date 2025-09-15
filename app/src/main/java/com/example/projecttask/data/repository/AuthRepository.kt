package com.example.projecttask.data.repository

import com.example.projecttask.data.api.RetrofitClient
import com.example.projecttask.domain.repository.AuthRepository
import com.example.projecttask.data.model.User
import io.reactivex.rxjava3.core.Observable
import android.util.Base64

class AuthRepositoryImpl : AuthRepository {
    override fun authenticate(username: String, password: String): Observable<User> {
        val authHeader = "Basic ${Base64.encodeToString("$username:$password".toByteArray(), Base64.NO_WRAP)}"
        return RetrofitClient.githubApiService.getUser(authHeader)
    }
}