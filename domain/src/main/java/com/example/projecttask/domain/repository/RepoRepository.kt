package com.example.projecttask.domain.repository

import com.example.projecttask.domain.model.Repo
import io.reactivex.rxjava3.core.Single

interface RepoRepository {
    fun getRepos(page: Int, perPage: Int): Single<List<Repo>>
}