package com.example.projecttask.feature_main.presentation.userdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.projecttask.feature_main.R
import com.example.projecttask.feature_main.databinding.FragmentUserDetailsBinding
import com.example.projecttask.feature_main.presentation.home.adapter.RepoAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailsFragment : Fragment() {

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserDetailsViewModel by viewModels()

    private val repoAdapter: RepoAdapter by lazy {
        RepoAdapter()
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()
        setupUserInfo()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        binding.repositoriesRecyclerView.adapter = repoAdapter
    }




    private fun setupUserInfo() {
        viewModel.username?.let { username ->
            binding.usernameTextView.text = username
        }

        viewModel.avatarUrl?.let { avatarUrl ->
            binding.userAvatarImageView.load(avatarUrl) {
                placeholder(R.drawable.ic_profile)
                error(R.drawable.ic_profile)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.repositories.observe(viewLifecycleOwner) { repos ->
            repoAdapter.submitList(repos)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (!errorMsg.isNullOrEmpty()) {
                Snackbar.make(
                    binding.root,
                    errorMsg,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}