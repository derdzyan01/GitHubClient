package com.example.projecttask.data.dto

import com.google.gson.annotations.SerializedName

internal data class RepoDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("stargazers_count") val stars: Int,
    @SerializedName("language") val language: String?
)