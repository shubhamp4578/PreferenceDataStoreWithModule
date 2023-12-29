package com.example.persistancestoredata

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.persistancestoredata.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var settings: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setListeners()
    }

    private fun init() {
        getFromSettings()
    }

    private fun setListeners() {
        with(binding) {
            saveSettings.setOnClickListener { saveToSettings() }
            reset.setOnClickListener { reset() }
        }
    }

    private fun saveToSettings() {
        if (::settings.isInitialized) {
            val name = binding.etInput.text?.toString() ?: ""
            lifecycleScope.launch {
                settings.putPreference(Constants.NAME_KEY, name)
            }
        }
    }

    private fun getFromSettings() {
        if (::settings.isInitialized) {
            lifecycleScope.launch {
                settings.getPreference(Constants.NAME_KEY, "").collectLatest {
                    binding.tvLabel.text = it.ifBlank { "No initial value/ Hard reset" }
                }
            }
        }
    }

    private fun reset() {
        lifecycleScope.launch {
            settings.clearAllPreference<String>()
        }
    }
}