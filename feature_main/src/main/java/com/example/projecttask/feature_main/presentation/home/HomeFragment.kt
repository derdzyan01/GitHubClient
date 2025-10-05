package com.example.projecttask.feature_main.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projecttask.feature_home.databinding.FragmentHomeBinding
import com.example.projecttask.feature_main.presentation.home.adapter.RepoAdapter
import com.example.projecttask.feature_main.presentation.home.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val repoAdapter: RepoAdapter by lazy { RepoAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.reposRecyclerView.apply {
            adapter = repoAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
                        firstVisibleItemPosition >= 0) {
                        viewModel.loadMoreRepos()
                    }
                }
            })
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshRepos()
        }
    }

    private fun observeViewModel() {
        viewModel.repos.observe(viewLifecycleOwner) { repos ->
            repoAdapter.submitList(repos)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading && repoAdapter.currentList.isEmpty()
            binding.swipeRefreshLayout.isRefreshing = isLoading && repoAdapter.currentList.isNotEmpty()
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (!errorMsg.isNullOrEmpty()) {
                Snackbar.make(binding.root, errorMsg, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}