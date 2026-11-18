package com.example.projecttask.data.dto

import com.google.gson.annotations.SerializedName

internal data class UserDto(
    @SerializedName("login")
    val login: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String
)