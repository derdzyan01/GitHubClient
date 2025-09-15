package com.example.projecttask.presentation.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projecttask.databinding.ActivityLoginBinding
import com.example.projecttask.presentation.state.LoginUiState
import com.example.projecttask.presentation.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.edit
import com.example.projecttask.data.LOGGED_IN
import com.example.projecttask.data.authDataStore

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInButton.setOnClickListener {
            val u = binding.usernameEditText.text.toString()
            val p = binding.passwordEditText.text.toString()
            if (u.isNotBlank() && p.isNotBlank()) viewModel.authenticate(u, p)
        }

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.signInButton.isEnabled = state !is LoginUiState.Loading
                when (state) {
                    is LoginUiState.Success -> {

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    is LoginUiState.Error ->
                        Snackbar.make(binding.root, "Invalid Credentials", Snackbar.LENGTH_LONG).show()
                    else -> Unit
                }
            }
        }
    }
}

