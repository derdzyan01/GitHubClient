package com.example.projecttask.data.repository

import io.reactivex.rxjava3.core.Single
import com.example.projecttask.data.AuthDataStoreManager
import com.example.projecttask.data.api.AuthInterceptor
import com.example.projecttask.data.api.GitHubApiService
import com.example.projecttask.data.mapper.toDomain
import com.example.projecttask.domain.model.User
import com.example.projecttask.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val dataStoreManager: AuthDataStoreManager,
    private val retrofitBuilder: Retrofit.Builder
) : AuthRepository {

    override fun authenticate(username: String, password: String): Single<User> {
        val authenticatedClient = okHttpClient.newBuilder()
            .addInterceptor(AuthInterceptor(username, password))
            .build()

        val authenticatedService = retrofitBuilder
            .client(authenticatedClient)
            .build()
            .create(GitHubApiService::class.java)

        return authenticatedService.getUser()
            .map { userDto -> userDto.toDomain() }
            .onErrorResumeNext { error ->
                Single.error(
                    when {
                        error.message?.contains("401") == true ->
                            Exception("invalid credentials")
                        error.message?.contains("403") == true ->
                            Exception("access forbidden")
                        error.message?.contains("network") == true ->
                            Exception("network error")
                        else -> Exception("login failed: ${error.message ?: "Unknown error"}")
                    }
                )
            }
    }

    override suspend fun setLoggedIn(isLoggedIn: Boolean) {
        dataStoreManager.setLoggedIn(isLoggedIn)
    }

    override fun isLoggedIn(): Flow<Boolean> = dataStoreManager.isLoggedIn()
}
