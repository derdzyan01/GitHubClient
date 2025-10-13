package com.example.projecttask.feature_main.presentation.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.projecttask.feature_main.R
import com.example.projecttask.feature_main.databinding.FragmentProfileBinding
import com.example.projecttask.feature_main.presentation.navigation.AppNavigator
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                requireContext().contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                viewModel.onAvatarSelected(it)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.logoutButton.setOnClickListener {
            viewModel.onLogoutClicked()
        }
        binding.profileImageView.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                binding.progressBar.isVisible = state.isLoading
                binding.usernameTextView.text = state.user?.login ?: "Loading..."

                if (!state.avatarUri.isNullOrEmpty()) {
                    binding.profileImageView.load(Uri.parse(state.avatarUri)) {
                        placeholder(R.drawable.ic_profile)
                        error(R.drawable.ic_profile)
                    }
                } else {
                    binding.profileImageView.setImageResource(R.drawable.ic_profile)
                }

                state.error?.let {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiEvent.collectLatest { event ->
                when (event) {
                    is ProfileUiEvent.LogoutSuccess -> {
                        (requireActivity() as? AppNavigator)?.onLogout()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}