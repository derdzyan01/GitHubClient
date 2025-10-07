package com.example.projecttask.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
internal interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repos: List<RepoEntity>): Completable

    @Query("SELECT * FROM repos ORDER BY stars DESC LIMIT :limit OFFSET :offset")
    fun getRepos(limit: Int, offset: Int): Single<List<RepoEntity>>

    @Query("DELETE FROM repos")
    fun clearAll(): Completable
}