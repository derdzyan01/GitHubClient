package com.example.projecttask.domain.model

data class Repo(
    val id: Long,
    val name: String,
    val description: String?,
    val stars: Int,
    val language: String?
)