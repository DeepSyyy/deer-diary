package com.example.deerdiary.ui.onboarding

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.deerdiary.data.datastore.DataStoreToken
import com.example.deerdiary.data.datastore.dataStore
import com.example.deerdiary.databinding.ActivityOnboardingBinding
import com.example.deerdiary.ui.homeScreen.HomeActivity
import com.example.deerdiary.ui.loginScreen.LoginActivity
import com.example.deerdiary.ui.registerScreen.RegisterActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    private lateinit var preference: DataStoreToken
    private val applicationScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference = DataStoreToken.getInstance(dataStore)

        playAnimation()
        binding()
    }

    private fun playAnimation() {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener {
                val value = it.animatedValue as Float
                binding.ivIlustrationOnboarding.alpha = value
            }
        }.start()
    }

    private fun binding() {
        binding.apply {
            btnLogin.setOnClickListener {
                val intent = Intent(this@OnboardingActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            btnRegister.setOnClickListener {
                val intent = Intent(this@OnboardingActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        applicationScope.launch {
            val user = preference.getSession().first().isLogin
            if (user) {
                val intent =
                    Intent(this@OnboardingActivity, HomeActivity::class.java).addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK,
                    )
                startActivity(intent)
                finish()
            }
        }
    }
}
