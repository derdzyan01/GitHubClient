package com.example.projecttask.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repos")
internal data class RepoEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String?,
    val stars: Int,
    val language: String?
)