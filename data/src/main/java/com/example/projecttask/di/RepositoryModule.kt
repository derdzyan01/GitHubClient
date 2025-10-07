package com.example.projecttask.di

import com.example.projecttask.data.repository.AuthRepositoryImpl
import com.example.projecttask.data.repository.RepoRepositoryImpl
import com.example.projecttask.domain.repository.AuthRepository
import com.example.projecttask.domain.repository.RepoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindRepoRepository(impl: RepoRepositoryImpl): RepoRepository
}