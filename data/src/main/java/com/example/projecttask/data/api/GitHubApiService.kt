package com.example.projecttask.data.api

import com.example.projecttask.data.dto.RepoDto
import com.example.projecttask.data.dto.UserDto
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface GitHubApiService {
    @GET("user")
    fun getUser(): Single<Response<UserDto>>

    @GET("user/repos")
    fun getRepos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<List<RepoDto>>
}