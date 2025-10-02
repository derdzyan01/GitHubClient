package com.example.projecttask.data.api

import com.example.projecttask.data.dto.RepoDto
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import com.example.projecttask.data.dto.UserDto
import retrofit2.http.Query

internal interface GitHubApiService {
    @GET("user")
    fun getUser(@Header("Authorization") auth: String): Single<Response<UserDto>>

    @GET("user/repos")
    fun getRepos(
        @Header("Authorization") auth: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<List<RepoDto>>
}
