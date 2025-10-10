package com.example.projecttask.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
internal data class UserEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "login") val origin: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String
)