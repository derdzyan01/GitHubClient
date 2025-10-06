package com.example.projecttask.data.api

import com.example.projecttask.data.AuthDataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authDataStoreManager: AuthDataStoreManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val credentials = getCurrentCredentials()

        return if (credentials != null) {
            val (username, password) = credentials
            val credential = Credentials.basic(username, password)
            val request = chain.request().newBuilder()
                .header("Authorization", credential)
                .build()
            chain.proceed(request)
        } else {
            chain.proceed(chain.request())
        }
    }

    private fun getCurrentCredentials(): Pair<String, String>? {
        return runBlocking {
            authDataStoreManager.getCredentials().first().let { (username, password) ->
                if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
                    Pair(username, password)
                } else {
                    null
                }
            }
        }
    }
}