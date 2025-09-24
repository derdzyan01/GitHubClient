package com.example.projecttask.feature_auth.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.projecttask.feature_auth.databinding.FragmentLoginBinding
import com.example.projecttask.feature_auth.presentation.state.LoginUiEvent
import com.example.projecttask.feature_auth.presentation.state.LoginUiState
import com.example.projecttask.feature_auth.presentation.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeUiState()
        observeUiEvents()
    }

    private fun setupClickListeners() {
        binding.signInButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                viewModel.authenticate(username, password)
            }
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.signInButton.isEnabled = state !is LoginUiState.Loading

                if (state is LoginUiState.Success) {
                    findNavController().navigate(
                        "app://home".toUri(),
                    )
                }
            }
        }
    }

    private fun observeUiEvents() {
        lifecycleScope.launch {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is LoginUiEvent.ShowError -> {
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
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