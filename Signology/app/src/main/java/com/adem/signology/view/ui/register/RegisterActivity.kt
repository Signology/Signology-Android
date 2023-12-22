package com.adem.signology.view.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.adem.signology.R
import com.adem.signology.data.remote.response.RegisterResponse
import com.adem.signology.databinding.ActivityRegisterBinding
import com.adem.signology.view.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.adem.signology.data.Result

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
// ...
// Initialize Firebase Aut
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupClickListeners()
        playAnimation()
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

    private fun setupClickListeners() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            register(name, email, password)
        }
    }

    private fun register(name: String, email: String, password: String) {
        registerViewModel.register(name, email, password).observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> handleSignupSuccess(result.data)
                is Result.Error -> handleSignupError()
            }
        }
    }

    private fun handleSignupSuccess(user: RegisterResponse) {
        showLoading(false)
        if (user.error == true) {
            Toast.makeText(this, user.message, Toast.LENGTH_SHORT).show()
        } else {
            showSuccessDialog()
        }
    }

    private fun handleSignupError() {
        showLoading(false)
        Toast.makeText(this, resources.getString(R.string.register_error), Toast.LENGTH_SHORT).show()
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage(R.string.register_done)
            val positiveButtonText = getString(R.string.next)
            val spannableString = SpannableString(positiveButtonText)
            val color = ContextCompat.getColor(context, R.color.purple)
            spannableString.setSpan(ForegroundColorSpan(color), 0, positiveButtonText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            setPositiveButton(spannableString) { _, _ ->
                finish()
            }
            create()
            show()
        }
    }

    private fun setupViewModel() {
        val viewModelFactory: ViewModelFactory = ViewModelFactory.getInstance(this)
        registerViewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]
    }

    private fun playAnimation() {
        val imageViewAnimator = ObjectAnimator.ofFloat(
            binding.imageView, View.TRANSLATION_X, -30f, 30f
        ).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        val title = ObjectAnimator.ofFloat(binding.tvWelcomeRegister, View.ALPHA, 1f).setDuration(100)
        val nameTextView = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signupButton = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title, nameTextView, nameEditTextLayout,
                emailTextView, emailEditTextLayout,
                passwordTextView, passwordEditTextLayout, signupButton
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
