package com.adem.signology.view.ui.profile.edit

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.adem.signology.R
import com.adem.signology.data.Result
import com.adem.signology.databinding.ActivityEditProfileBinding
import com.adem.signology.view.ViewModelFactory
import com.adem.signology.util.reduceFileImage
import com.adem.signology.util.uriToFile
import com.adem.signology.view.ui.premium.PremiumActivity
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel by viewModels<EditProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var getFile: File? = null

    private var accountType:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f


        val btnEdit: Button = binding.btnEdit

        val imageView: ImageView = binding.imageView3

        viewModel.getUserById().observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    // Update the UI with the new data
                    accountType = result.data.user?.accType.toString()
                    binding.tvProfileAccType.text = result.data.user?.accType
                    Glide.with(this)
                        .load(result.data.user?.profilePic)
                        .fitCenter()
                        .circleCrop()
                        .into(imageView)
                }
                // Handle other result cases if needed
                else -> {}
            }
        }
        // Set an OnClickListener for the ImageView to trigger image selection
        imageView.setOnClickListener {
            startGallery()
        }

        btnEdit.setOnClickListener {
            handleProfileEditing()
        }

        binding.btnPremium.setOnClickListener {
            showDialogBox()
        }


    }



    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherGallery.launch(chooser)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@EditProfileActivity)
            getFile = myFile
            binding.imageView3.setImageURI(selectedImg)
        }
    }


    private fun handleProfileEditing() {
        val newName = binding.nameEditText.text.toString().trim()

        if (newName.isNotEmpty() || getFile != null) {

            val newUsername = newName.toRequestBody("text/plain".toMediaType())

            val file = reduceFileImage(getFile as File)
            val imageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart = MultipartBody.Part.createFormData("profile_pic", file.name, imageFile)

            viewModel.editProfile( newUsername, imageMultipart).observe(this) { result ->
                // Handle the result directly here
                when (result) {
                    is Result.Success -> {
                        showToast("Profile updated successfully")
                        finish()
                    }
                    is Result.Error -> {
                        showToast("Error updating profile: ${result.error}")
                    }

                    else -> {}
                }
            }
        } else {
            // If the name is empty, show an error in the TextInputLayout
            binding.nameEditTextLayout.error = "Name cannot be empty"
        }
    }

    private fun showDialogBox() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_premium)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnPremium: Button = dialog.findViewById(R.id.btnPremium)
        val tvPremiumContent: TextView = dialog.findViewById(R.id.tvPremiumContent)

        if(accountType == "free") {
            tvPremiumContent.text = getString(R.string.dialog_acc_free)
        } else {
            tvPremiumContent.text = getString(R.string.dialog_acc_premium)
        }
        btnPremium.setOnClickListener {
            startActivity(Intent(this, PremiumActivity::class.java))
            dialog.dismiss()
        }
        dialog.show()
    }



    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val IMAGE_PICK_REQUEST_CODE = 123
    }
}
