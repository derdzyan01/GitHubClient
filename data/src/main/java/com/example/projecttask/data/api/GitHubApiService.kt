package com.example.projecttask.data.api

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import com.example.projecttask.data.dto.UserDto

interface GitHubApiService {
    @GET("user")
    fun getUser(@Header("Authorization") auth: String): Single<Response<UserDto>>
}
