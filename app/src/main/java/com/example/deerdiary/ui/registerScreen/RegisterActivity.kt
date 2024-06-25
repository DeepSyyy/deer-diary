package com.example.deerdiary.ui.registerScreen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.deerdiary.ViewModelFactory
import com.example.deerdiary.data.repository.Resource
import com.example.deerdiary.databinding.ActivityRegisterBinding
import com.example.deerdiary.ui.loginScreen.LoginActivity
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val factory by lazy { ViewModelFactory.getInstance(this) }

    private val registerViewModel: RegisterViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding()
    }

    private fun binding() {
        binding.apply {
            btnLogin.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            btnRegister.setOnClickListener {
                val name = edRegisterName.text.toString()
                val email = edRegisterEmail.text.toString()
                val password = edRegisterPassword.text.toString()
                try {
                    lifecycleScope.launch {
                        registerViewModel.register(name, email, password)
                            .observe(this@RegisterActivity) { resource ->
                                if (resource != null) {
                                    when (resource) {
                                        is Resource.Loading -> {
                                        }

                                        is Resource.Success -> {
                                            showToast("Register berhasil! Silahkan login")
                                            val intent =
                                                Intent(
                                                    this@RegisterActivity,
                                                    LoginActivity::class.java,
                                                )
                                            intent.addFlags(
                                                Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                    .or(Intent.FLAG_ACTIVITY_NEW_TASK),
                                            )
                                            startActivity(intent)
                                        }

                                        is Resource.Error -> {
                                            showToast(resource.error)
                                        }
                                    }
                                }
                            }
                    }
                } catch (e: Exception) {
                    showToast(e.message.toString())
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
