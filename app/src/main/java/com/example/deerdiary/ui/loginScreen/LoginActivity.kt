package com.example.deerdiary.ui.loginScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.deerdiary.ViewModelFactory
import com.example.deerdiary.data.datastore.DataStoreToken
import com.example.deerdiary.data.datastore.UserModelDataStore
import com.example.deerdiary.data.datastore.dataStore
import com.example.deerdiary.data.repository.Resource
import com.example.deerdiary.databinding.ActivityLoginBinding
import com.example.deerdiary.ui.homeScreen.HomeActivity
import com.example.deerdiary.ui.registerScreen.RegisterActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val factory by lazy { ViewModelFactory.getInstance(this) }

    private val loginViewModel: LoginViewModel by viewModels { factory }

    private val dataStoreToken by lazy { DataStoreToken }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding()
    }

    private fun binding() {
        binding.apply {
            btnRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
            btnLogin.setOnClickListener {
                val email = edLoginEmail.text.toString()
                val password = edLoginPassword.text.toString()
                try {
                    lifecycleScope.launch {
                        loginViewModel.login(email, password)
                            .observe(this@LoginActivity) { resource ->
                                if (resource != null) {
                                    when (resource) {
                                        is Resource.Loading -> {
                                            progressBar.visibility = android.view.View.VISIBLE
                                        }

                                        is Resource.Success -> {
                                            progressBar.visibility = android.view.View.GONE
                                            showToast("Login berhasil! Selamat datang ${resource.data.loginResult?.name}")
                                            lifecycleScope.launch {
                                                save(
                                                    UserModelDataStore(
                                                        resource.data.loginResult?.token.toString(),
                                                        resource.data.loginResult?.userId.toString(),
                                                        resource.data.loginResult?.name.toString(),
                                                        resource.data.loginResult?.token?.isNotEmpty()
                                                            ?: false,
                                                    ),
                                                )
                                                Log.d(
                                                    "LoginActivity",
                                                    "token: ${
                                                        dataStoreToken.getInstance(dataStore)
                                                            .getSession().first().token
                                                    }",
                                                )

                                                val intent =
                                                    Intent(
                                                        this@LoginActivity,
                                                        HomeActivity::class.java,
                                                    )
                                                intent.addFlags(
                                                    Intent.FLAG_ACTIVITY_CLEAR_TOP.or(
                                                        Intent.FLAG_ACTIVITY_CLEAR_TASK,
                                                    ).or(Intent.FLAG_ACTIVITY_NEW_TASK),
                                                )
                                                startActivity(intent)
                                            }
                                        }

                                        is Resource.Error -> {
                                            progressBar.visibility = android.view.View.GONE
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

    private fun save(userModel: UserModelDataStore) {
        lifecycleScope.launch {
            loginViewModel.saveSession(
                UserModelDataStore(
                    userModel.token,
                    userModel.userId,
                    userModel.nama,
                    userModel.isLogin,
                ),
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
