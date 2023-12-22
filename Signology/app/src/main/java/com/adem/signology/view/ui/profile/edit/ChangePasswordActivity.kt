package com.adem.signology.view.ui.profile.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.adem.signology.R
import com.adem.signology.data.Result
import com.adem.signology.databinding.ActivityChangePasswordBinding
import com.adem.signology.view.ViewModelFactory
import com.adem.signology.view.ui.camera.PredictViewModel
import com.google.android.material.textfield.TextInputEditText

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModel by viewModels<ChangePasswordViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f

        binding.btnUpdatePassword.setOnClickListener {

            val currentPassword = binding.currentPasswordEditText.text.toString().trim()
            val newPassword = binding.newPasswordEditText.text.toString().trim()
            val retypeNewPassword = binding.retypeNewPasswordEditText.text.toString().trim()

            if (newPassword == retypeNewPassword) {
                // Passwords match, proceed with the update
                viewModel.updatePassword(newPassword, currentPassword).observe(this) { updateResult ->
                    // Handle the result directly here
                    when (updateResult) {
                        is Result.Success -> {
                            showToast("Password updated successfully: ${updateResult.data.message}")
                            finish()
                        }
                        is Result.Error -> {
                            showToast("Error updating password: ${updateResult.error}")
                            println(updateResult.error)
                        }
                        // Handle other cases if needed
                        else -> {}
                    }
                }
            } else {
                // Passwords do not match, show a Toast message
                showToast("Passwords do not match. Please retype the password.")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}