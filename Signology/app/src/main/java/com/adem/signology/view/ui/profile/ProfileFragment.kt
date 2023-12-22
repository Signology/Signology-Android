package com.adem.signology.view.ui.profile

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.adem.signology.R
import com.adem.signology.databinding.FragmentProfileBinding
import com.adem.signology.view.ViewModelFactory
import com.adem.signology.view.ui.profile.edit.EditProfileActivity
import com.adem.signology.view.welcome.WelcomeActivity
import com.adem.signology.data.Result
import com.adem.signology.data.model.FreeUserModel
import com.adem.signology.view.ui.history.HistoryActivity
import com.adem.signology.view.ui.premium.PremiumActivity
import com.adem.signology.view.ui.profile.edit.ChangePasswordActivity
import com.bumptech.glide.Glide


class ProfileFragment : Fragment() {

private var _binding: FragmentProfileBinding? = null
  private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var accType:String = ""

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {

    _binding = FragmentProfileBinding.inflate(inflater, container, false)
    val root: View = binding.root


        viewModel.getUserById().observe(requireActivity()) { result ->
            // Handle result
            println("GETDATA = $result")

            when (result) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.contentProfile.visibility = View.VISIBLE
                    println("SUCCESS GET DATA ID")
                    // Handle successful result
                    binding.tvName.text = result.data.user?.username
                    binding.tvEmail.text = result.data.user?.email
                    accType = result.data.user?.accType.toString()
                    println("accType: $accType")
                    if (result.data.user?.profilePic != null && result.data.user.profilePic.isNotEmpty()) {
                        // If profilePic is not null, load it using Glide
                        Glide.with(this)
                            .load(result.data.user.profilePic)
                            .fitCenter()
                            .circleCrop()
                            .into(binding.ivProfile)
                        if(result.data.user.accType != "free") {
                            binding.ivProfile.borderColor = Color.parseColor("#FFEDBB")
                        } else {
                            binding.ivProfile.borderColor = Color.parseColor("#D9D9D9")
                        }
                    } else {
                        Glide.with(this)
                            .load(ContextCompat.getDrawable(requireContext(), R.drawable.ic_image))
                            .fitCenter()
                            .circleCrop()
                            .into(binding.ivProfile)

                        val accType = result.data.user?.accType ?: ""
                        val point = result.data.user?.point ?: 0
                        val freeUserModel = FreeUserModel(point, accType)
                        viewModel.savePointAccSession(freeUserModel)
                    }
                }

                is Result.Loading -> {
                    // Show loading indicator if needed
                    binding.progressBar.visibility = View.VISIBLE
                    binding.contentProfile.visibility = View.GONE
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.contentProfile.visibility = View.VISIBLE
                    // Handle error result
                    val errorMessage = result.error
                    // Show an error message or handle it accordingly
                }

                else -> {}
            }
        }

      val actionLogout: CardView = binding.cvLogout
      actionLogout.setOnClickListener {
          showDialogBox()
      }


      val actionEditProfile: CardView = binding.cvEdit
      actionEditProfile.setOnClickListener{
          val intent = Intent(requireActivity(), EditProfileActivity::class.java)
          startActivity(intent)

      }

        val actionChangePassword: CardView = binding.cvPass
        actionChangePassword.setOnClickListener{
            val intent = Intent(requireActivity(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        val actionHistoryTranslate: CardView = binding.cvHistory
        println("accType: $accType")


        actionHistoryTranslate.setOnClickListener {
            if (accType != "free") {
                val intent = Intent(requireActivity(), HistoryActivity::class.java)
                startActivity(intent)
            } else {
                showDialogBoxPremium()
            }
        }
    return root
  }


    override fun onResume() {
        super.onResume()
        // Refresh the user data when the fragment is resumed
        viewModel.getUserById().observe(requireActivity()) { result ->
            when (result) {
                is Result.Success -> {
                    // Update the UI with the new data
                    binding.tvName.text = result.data.user?.username
                    binding.tvEmail.text = result.data.user?.email
                    Glide.with(this)
                        .load(result.data.user?.profilePic)
                        .circleCrop()
                        .into(binding.ivProfile)
                }
                // Handle other result cases if needed
                else -> {}
            }
        }
    }

    private fun showDialogBoxPremium() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_premium)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnPremium: Button = dialog.findViewById(R.id.btnPremium)
        btnPremium.setOnClickListener {
            startActivity(Intent(requireActivity(), PremiumActivity::class.java))
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showDialogBox() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.logout_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnYes: Button = dialog.findViewById(R.id.btn_yes)
        val btnNo: Button = dialog.findViewById(R.id.btn_no)

        btnYes.setOnClickListener {
            handleLogout()
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun handleLogout() {
        viewModel.logout()
        val intent = Intent(requireActivity(), WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.finish()
    }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}