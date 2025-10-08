package com.example.projecttask.data.repository

import com.example.projecttask.data.AuthDataStoreManager
import com.example.projecttask.data.api.AuthInterceptor
import com.example.projecttask.data.api.GitHubApiService
import com.example.projecttask.data.dto.UserDto
import com.example.projecttask.data.mapper.toDomain
import com.example.projecttask.domain.model.User
import com.example.projecttask.domain.repository.AuthRepository
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx3.rxCompletable
import okhttp3.Credentials
import retrofit2.Response
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val dataStoreManager: AuthDataStoreManager,
    private val gitHubApiService: GitHubApiService,
) : AuthRepository {

    override fun authenticate(username: String, password: String): Single<User> {
        val authToken = Credentials.basic(username, password)

        return gitHubApiService.getUser(authToken)
            .flatMap { response: Response<UserDto> ->
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val saveCredentialsCompletable = rxCompletable(Dispatchers.IO) {
                            dataStoreManager.saveCredentials(username, password)
                        }
                        saveCredentialsCompletable.toSingleDefault(body.toDomain())
                    } else {
                        Single.error(Exception("empty response"))
                    }
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