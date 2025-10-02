package com.example.projecttask.di

import android.content.Context
import androidx.room.Room
import com.example.projecttask.data.local.AppDatabase
import com.example.projecttask.data.local.RepoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "projecttask_database"
        ).build()
    }

    @Provides
    fun provideRepoDao(database: AppDatabase): RepoDao {
        return database.repoDao()
    }
}