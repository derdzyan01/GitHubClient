package com.example.projecttask.data.repository

import io.reactivex.rxjava3.core.Single
import com.example.projecttask.data.AuthDataStoreManager
import com.example.projecttask.data.api.GitHubApiService
import com.example.projecttask.data.dto.UserDto
import com.example.projecttask.data.mapper.toDomain
import com.example.projecttask.domain.model.User
import com.example.projecttask.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.Credentials
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataStoreManager: AuthDataStoreManager,
    private val gitHubApiService: GitHubApiService
) : AuthRepository {

    override fun authenticate(username: String, password: String): Single<User> {
        val authHeader = Credentials.basic(username, password)

        return gitHubApiService.getUser(authHeader)
            .flatMap { response: Response<UserDto> ->
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) Single.just(body.toDomain())
                    else Single.error(Exception("empty response"))
                } else {
                    Single.error(
                        when (response.code()) {
                            401 -> Exception("invalid credentials")
                            403 -> Exception("access forbidden")
                            else -> Exception("login failed: ${response.code()}")
                        }
                    )
                }
            }
    }

    override suspend fun setLoggedIn(isLoggedIn: Boolean) {
        dataStoreManager.setLoggedIn(isLoggedIn)
    }

    override fun isLoggedIn(): Flow<Boolean> = dataStoreManager.isLoggedIn()
}
