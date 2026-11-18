package com.example.projecttask.data.mapper

import com.example.projecttask.data.dto.UserDto
import com.example.projecttask.data.local.UserEntity
import com.example.projecttask.domain.model.User

internal fun UserDto.toDomain() = User(
    login = login,
    id = id,
    avatarUrl = avatarUrl
)

internal fun UserDto.toEntity() = UserEntity(
    id = this.id,
    origin = this.login,
    avatarUrl = this.avatarUrl
)

internal fun UserEntity.toDomain() = User(
    id = this.id,
    login = this.origin,
    avatarUrl = this.avatarUrl
)