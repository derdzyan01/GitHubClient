package com.example.projecttask.data.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import com.example.projecttask.data.dto.UserDto

interface GitHubApiService {
    @GET("user")
    fun getUser(): Single<UserDto>
}