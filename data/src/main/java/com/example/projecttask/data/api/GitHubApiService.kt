package com.example.projecttask.data.api

import com.example.projecttask.data.dto.RepoDto
import com.example.projecttask.data.dto.UserDto
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

internal interface GitHubApiService {
    @GET("user")
    fun getUser(): Single<Response<UserDto>>

    @GET("user/repos")
    fun getRepos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<List<RepoDto>>

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ) : List<UserDto>

    @GET("users/{username}/repos")
    fun getUserRepos(
        @Path("username") username: String,
        @Query("per_page") perPage: Int = 100
    ) : Single<List<RepoDto>>
}