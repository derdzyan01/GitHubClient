package com.example.projecttask.data.api

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import com.example.projecttask.data.model.User

interface GitHubApiService {
    @GET("user")
    fun getUser(@Header("Authorization") authHeader: String): Observable<User>
}