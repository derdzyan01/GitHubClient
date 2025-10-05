package com.example.projecttask.data

import com.example.projecttask.data.api.AuthInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptorManager @Inject constructor(
    private val authInterceptor: AuthInterceptor,
    private val authDataStoreManager: AuthDataStoreManager
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    suspend fun initialize() {
        val (username, password) = authDataStoreManager.getCredentials().first()
        if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
            authInterceptor.updateCredentials(username, password)
        } else {
            authInterceptor.clearCredentials()
        }

        scope.launch {
            authDataStoreManager.getCredentials().collect { (username, password) ->
                if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
                    authInterceptor.updateCredentials(username, password)
                } else {
                    authInterceptor.clearCredentials()
                }
            }
        }
    }

    fun cancel() {
        scope.cancel()
    }
}