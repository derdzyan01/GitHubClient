package com.example.projecttask.di

import com.example.projecttask.data.AuthDataStoreManager
import com.example.projecttask.data.AuthInterceptorManager
import com.example.projecttask.data.api.AuthInterceptor
import com.example.projecttask.data.repository.AuthRepositoryImpl
import com.example.projecttask.data.repository.RepoRepositoryImpl
import com.example.projecttask.domain.repository.AuthRepository
import com.example.projecttask.domain.repository.RepoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindRepoRepository(impl: RepoRepositoryImpl): RepoRepository
}