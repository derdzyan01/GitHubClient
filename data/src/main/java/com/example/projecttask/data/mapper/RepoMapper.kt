package com.example.projecttask.data.mapper

import com.example.projecttask.data.dto.RepoDto
import com.example.projecttask.data.local.RepoEntity
import com.example.projecttask.domain.model.Repo

internal fun RepoDto.toEntity(): RepoEntity = RepoEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    stars = this.stars,
    language = this.language
)

internal fun RepoEntity.toDomain(): Repo = Repo(
    id = this.id,
    name = this.name,
    description = this.description,
    stars = this.stars,
    language = this.language
)