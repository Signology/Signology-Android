package com.adem.signology.view.ui.home

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.adem.signology.R
import com.adem.signology.data.Result
import com.adem.signology.data.model.FreeUserModel
import com.adem.signology.data.model.UserModel
import com.adem.signology.data.remote.response.UserResponse
import com.adem.signology.databinding.FragmentHomeBinding
import com.adem.signology.view.ViewModelFactory
import com.adem.signology.view.ui.camera.CameraActivity
import com.adem.signology.view.ui.learn.ListLetterActivity
import com.adem.signology.view.ui.premium.PremiumActivity
import com.bumptech.glide.Glide

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var accountType:String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(CameraActivity.REQUIRED_PERMISSION)
        }

        observeUserData()

        setCardClickListeners()



        return root
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            CameraActivity.REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED


    private fun observeUserData() {
        viewModel.getUserById().observe(requireActivity()) { result ->

            handleUserDataResult(result)
        }
    }

    private fun handleUserDataResult(result: Result<UserResponse>) {
        when (result) {
            is Result.Success -> {
                updateUI(result.data)
                accountType = result.data.user?.accType.toString()
                val point = result.data.user?.point ?: 0
                val freeUserModel = FreeUserModel(point, accountType)
                viewModel.savePointAccSession(freeUserModel)
            }
            is Result.Loading -> {
                // Handle loading if needed
            }
            is Result.Error -> {
                // Handle error result
                val errorMessage = result.error
                // Show an error message or handle it accordingly
            }
            else -> {
            }
        }
    }

    private fun updateUI(user: UserResponse) {
        user.let {
            val accTypeString = getString(R.string.acc_type)
            val formattedAccType = accTypeString.format(it.user?.accType)
            val pointString = getString(R.string.point)
            val formattedPoint = pointString.format(it.user?.point.toString())
            val premiumExpired = getString(R.string.until)
            val formattedPremiumExpired = premiumExpired.format(it.user?.premiumDate.toString())
            binding.accType.text = formattedAccType
            if (it.user?.accType == "Free" || it.user?.accType == "Student") {
                binding.accPoint.text = formattedPoint
            } else {
                binding.accPoint.text = formattedPremiumExpired
            }


            if (it.user?.profilePic != null && it.user.profilePic.isNotEmpty()) {
                // If profilePic is not null, load it using Glide
                Glide.with(this)
                    .load(it.user.profilePic)
                    .fitCenter()
                    .circleCrop()
                    .into(binding.ivProfileHome)
            } else {
                // If profilePic is null, load the default image
                Glide.with(this)
                    .load(ContextCompat.getDrawable(requireContext(), R.drawable.ic_image))
                    .fitCenter()
                    .circleCrop()
                    .into(binding.ivProfileHome)
            }
        }
    }

    private fun setCardClickListeners() {
        binding.cardLearn.setOnClickListener {
            // Start LearnActivity when the card is clicked
            startActivity(Intent(activity, ListLetterActivity::class.java))
        }

        binding.cvPremium.setOnClickListener {
            showDialogBox()
        }

        binding.videoCard.setOnClickListener {
            val videoUrl = "https://youtu.be/HcmxDztnKsg?si=zyBhSi1WHwbEwwK3"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))

            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "No app found to handle the YouTube video.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDialogBox() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_premium)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnPremium: Button = dialog.findViewById(R.id.btnPremium)
        val tvPremiumContent: TextView = dialog.findViewById(R.id.tvPremiumContent)

        if(accountType == "Free") {
            tvPremiumContent.text = getString(R.string.dialog_acc_free)
        } else {
            tvPremiumContent.text = getString(R.string.dialog_acc_premium)
        }
        btnPremium.setOnClickListener {
            startActivity(Intent(activity, PremiumActivity::class.java))
            dialog.dismiss()
        }

        dialog.show()
    }


    override fun onResume() {
        super.onResume()
        observeUserData()
    }

    companion object {
        const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
