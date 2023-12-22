package com.adem.signology.view.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.adem.signology.view.onboarding.OnBoardingActivity
import com.adem.signology.view.ViewModelFactory

import com.adem.signology.data.Result
import com.adem.signology.data.model.UserModel
import com.adem.signology.data.remote.response.LoginResponse
import com.adem.signology.R
import com.adem.signology.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        playAnimation()
        setupAction()
    }

    private fun setupView() {
        setupFullScreen()
        supportActionBar?.hide()
        showLoading(false)
    }

    private fun setupFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            viewModel.login(email, password).observe(this) { result ->
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> handleLoginSuccess(result.data)
                    is Result.Error -> handleLoginError()
                    else -> {}
                }
            }

        }
    }

    private fun handleLoginSuccess(user: LoginResponse) {
        showLoading(false)
        if (user.error == true) {
            Toast.makeText(this, user.message, Toast.LENGTH_SHORT).show()
        } else {
            val id = user.loginResult?.id ?: 0
            val name = user.loginResult?.username ?: ""
            val email = user.loginResult?.email ?: ""
            val token = user.loginResult?.token ?: ""
            val accType = user.loginResult?.accType ?: "Free"
            val point = user.loginResult?.point ?: 500
            val userModel = UserModel(id, name, email, token, point, accType,0, true)
            viewModel.saveSession(userModel)

            val intent = Intent(this@LoginActivity, OnBoardingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun handleLoginError() {
        showLoading(false)
        Toast.makeText(
            this,
            resources.getString(R.string.login_error),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun playAnimation() {
        val imageViewAnimator = ObjectAnimator.ofFloat(
            binding.imageView, View.TRANSLATION_X, -30f, 30f
        ).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }


        val title = ObjectAnimator.ofFloat(binding.tvWelcomeLogin, View.ALPHA, 1f).setDuration(100)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val loginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title, emailTextView, emailEditTextLayout,
                passwordTextView, passwordEditTextLayout, loginButton
            )
            startDelay = 100
            start()
        }

        imageViewAnimator.start()
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}