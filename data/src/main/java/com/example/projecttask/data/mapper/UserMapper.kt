package com.example.projecttask.data.mapper

import com.example.projecttask.data.dto.UserDto
import com.example.projecttask.domain.model.User

fun UserDto.toDomain() = User(
    login = login,
    id = id,
    avatarUrl = avatarUrl
)