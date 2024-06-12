package com.example.deerdiary.ui.settingScreen

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.deerdiary.ViewModelFactory
import com.example.deerdiary.databinding.ActivitySettingBinding
import com.example.deerdiary.ui.loginScreen.LoginActivity
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    private val factory by lazy { ViewModelFactory.getInstance(this) }

    private val settingViewModel: SettingViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setLogOutButton()
        setupChangeLanguageButton()
    }

    private fun setLogOutButton() {
        binding.actionLogout.setOnClickListener {
            try {
                lifecycleScope.launch {
                    settingViewModel.deleteSession()
                    val intent = Intent(this@SettingActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_CLEAR_TASK) or (Intent.FLAG_ACTIVITY_NEW_TASK))
                    Toast.makeText(this@SettingActivity, "Logout berhasil", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(intent)
                    finish()
                }
            } catch (e: Exception) {
                Log.e("SettingActivity", "Error: ${e.message}")
            }
        }
    }

    private fun setupChangeLanguageButton() {
        binding.btnGantiBahasa.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }
}
