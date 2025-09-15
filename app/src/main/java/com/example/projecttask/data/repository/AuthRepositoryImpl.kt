package com.example.projecttask.data.repository

import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import com.example.projecttask.data.api.AuthInterceptor
import com.example.projecttask.data.api.GitHubApiService
import com.example.projecttask.data.mapper.toDomain
import com.example.projecttask.domain.model.User
import com.example.projecttask.domain.repository.AuthRepository
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val service: GitHubApiService,
    private val okHttpClient: OkHttpClient
) : AuthRepository {

    override fun authenticate(username: String, password: String): Single<User> =
        okHttpClient.newBuilder()
            .addInterceptor(AuthInterceptor(username, password))
            .build()
            .let { newClient ->
                Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .client(newClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()
                    .create(GitHubApiService::class.java)
            }
            .getUser()
            .map { it.toDomain() }
}