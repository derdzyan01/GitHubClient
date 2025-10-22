package com.example.projecttask.feature_main.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.projecttask.data.ThemeMode
import com.example.projecttask.feature_main.R
import com.example.projecttask.feature_main.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()

    private val themeCheckedListener = RadioGroup.OnCheckedChangeListener { _, checkedId ->
        val themeMode = when (checkedId) {
            R.id.radioLightTheme -> ThemeMode.LIGHT
            R.id.radioDarkTheme -> ThemeMode.DARK
            R.id.radioSystemTheme -> ThemeMode.SYSTEM
            else -> ThemeMode.SYSTEM
        }
        viewModel.setTheme(themeMode)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupThemeSelection()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }




    private fun setupThemeSelection() {
        binding.themeRadioGroup.setOnCheckedChangeListener(themeCheckedListener)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentTheme.collectLatest { theme ->
                val newId = when (theme) {
                    ThemeMode.LIGHT -> R.id.radioLightTheme
                    ThemeMode.DARK -> R.id.radioDarkTheme
                    ThemeMode.SYSTEM -> R.id.radioSystemTheme
                }



                if (binding.themeRadioGroup.checkedRadioButtonId != newId) {
                    binding.themeRadioGroup.setOnCheckedChangeListener(null)
                    binding.themeRadioGroup.check(newId)
                    binding.themeRadioGroup.setOnCheckedChangeListener(themeCheckedListener)
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}