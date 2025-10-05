package com.example.projecttask.data.api

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    private val _credentials = MutableStateFlow<Pair<String, String>?>(null)
    val credentials: StateFlow<Pair<String, String>?> = _credentials

    fun updateCredentials(username: String, password: String) {
        _credentials.value = Pair(username, password)
    }

    fun clearCredentials() {
        _credentials.value = null
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentCredentials = _credentials.value
        return if (currentCredentials != null) {
            val (username, password) = currentCredentials
            val credential = Credentials.basic(username, password)
            val request = chain.request().newBuilder()
                .header("Authorization", credential)
                .build()
            chain.proceed(request)
        } else {
            chain.proceed(chain.request())
        }
    }
}