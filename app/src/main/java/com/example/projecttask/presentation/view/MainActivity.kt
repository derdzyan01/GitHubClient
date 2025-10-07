package com.example.projecttask.presentation.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.projecttask.R
import com.example.projecttask.databinding.ActivityMainBinding
import com.example.projecttask.presentation.viewmodel.MainViewModel
import com.example.projecttask.presentation.viewmodel.NavigationEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val splash = installSplashScreen()
        var keepSplashOnScreen = true
        splash.setKeepOnScreenCondition { keepSplashOnScreen }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.navigationEvent.collect { event ->
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController
                val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

                navGraph.setStartDestination(
                    when (event) {
                        is NavigationEvent.ToHomeScreen -> R.id.mainFragment
                        is NavigationEvent.ToLoginScreen -> com.example.projecttask.feature_auth.R.id.nav_graph_auth
                    }
                )

                navController.graph = navGraph
                keepSplashOnScreen = false
            }
        }

        viewModel.decideInitialScreen()
    }
}