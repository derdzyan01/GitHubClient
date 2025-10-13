package com.example.projecttask.data.repository

import com.example.projecttask.data.AuthDataStoreManager
import com.example.projecttask.data.api.GitHubApiService
import com.example.projecttask.data.mapper.toDomain
import com.example.projecttask.domain.model.User
import com.example.projecttask.domain.repository.AuthRepository
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx3.rxCompletable
import javax.inject.Inject


internal class AuthRepositoryImpl @Inject constructor(
    private val dataStoreManager: AuthDataStoreManager,
    private val gitHubApiService: GitHubApiService,
) : AuthRepository {

    override fun authenticate(username: String, password: String): Single<User> {
        val saveCredentialsCompletable = rxCompletable(Dispatchers.IO) {
            dataStoreManager.saveCredentials(username, password)
        }

        return saveCredentialsCompletable
            .andThen(gitHubApiService.getUser())
            .flatMap { response ->
                if (response.isSuccessful) {
                    response.body()?.let { Single.just(it.toDomain()) }
                        ?: Single.error(Exception("Empty response"))
                } else {
                    Single.error(
                        when (response.code()) {
                            401 -> Exception("invalid credentials")
                            403 -> Exception("access not granted")
                            else -> Exception("Login failed: ${response.code()}")
                        }
                    )
                }
            }
    }

    override suspend fun setLoggedIn(isLoggedIn: Boolean) {
        dataStoreManager.setLoggedIn(isLoggedIn)
    }

    override fun isLoggedIn(): Flow<Boolean> = dataStoreManager.isLoggedIn()

    override fun getCurrentUser(): Single<User> {
        return gitHubApiService.getUser()
            .flatMap { response ->
                if (response.isSuccessful) {
                    response.body()?.let { Single.just(it.toDomain()) }
                        ?: Single.error(Exception("no response"))
                } else {
                    Single.error(Exception("failed to get user: ${response.code()}"))
                }
            }
    }

    override suspend fun clearCredentials() {
        dataStoreManager.clearCredentials()
    }

    override suspend fun saveAvatarUri(uri: String) {
        dataStoreManager.saveAvatarUri(uri)
    }

    override fun getAvatarUri(): Flow<String?> {
        return dataStoreManager.getAvatarUri()
    }
}