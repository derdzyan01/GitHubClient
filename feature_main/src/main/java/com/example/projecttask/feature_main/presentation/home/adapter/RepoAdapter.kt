package com.example.projecttask.feature_main.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projecttask.domain.model.Repo
import com.example.projecttask.feature_main.R
import com.example.projecttask.feature_main.databinding.ItemRepoBinding

class RepoAdapter : ListAdapter<Repo, RepoAdapter.RepoViewHolder>(RepoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RepoViewHolder(private val binding: ItemRepoBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: Repo) {
            binding.repoName.text = repo.name
            binding.repoDescription.text = repo.description ?: context.getString(R.string.no_description)
            binding.repoStars.text = repo.stars.toString()
            binding.repoLanguage.text = repo.language ?: context.getString(R.string.no_language)
        }
    }

    class RepoDiffCallback : DiffUtil.ItemCallback<Repo>() {
        override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem == newItem
    }
}