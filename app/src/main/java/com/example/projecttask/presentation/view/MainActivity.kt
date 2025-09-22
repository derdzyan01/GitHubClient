package com.example.projecttask.presentation.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.projecttask.R
import com.example.projecttask.databinding.ActivityMainBinding
import com.example.projecttask.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splash.setKeepOnScreenCondition { true }

        lifecycleScope.launch {
            val isLoggedIn = viewModel.isLoggedIn().first()
            splash.setKeepOnScreenCondition { false }

            val navController = findNavController(R.id.nav_host_fragment)
            if (isLoggedIn) {
                navController.navigate(R.id.homeFragment)
            } else {
                navController.navigate(R.id.loginFragment)
            }
        }
    }
}
