package com.example.projecttask.feature_home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projecttask.domain.model.Repo
import com.example.projecttask.feature_home.databinding.ItemRepoBinding

class RepoAdapter : ListAdapter<Repo, RepoAdapter.RepoViewHolder>(RepoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RepoViewHolder(private val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: Repo) {
            binding.repoName.text = repo.name
            binding.repoDescription.text = repo.description ?: "No description available."
            binding.repoStars.text = repo.stars.toString()
            binding.repoLanguage.text = repo.language ?: "No language."
        }
    }

    class RepoDiffCallback : DiffUtil.ItemCallback<Repo>() {
        override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem == newItem
    }
}