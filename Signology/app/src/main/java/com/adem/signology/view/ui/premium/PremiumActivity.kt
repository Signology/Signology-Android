package com.adem.signology.view.ui.premium

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.adem.signology.R
import com.adem.signology.data.Result
import com.adem.signology.data.model.premium.PremiumModel
import com.adem.signology.databinding.ActivityPremiumBinding
import com.adem.signology.view.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PremiumActivity : AppCompatActivity() {

    private val viewModel by viewModels<PremiumViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityPremiumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f

        viewModel.premiumData.observe(this, Observer { premiumData ->
            updateUI(premiumData)
        })


        viewModel.fetchPremiumData()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    private fun addOneMonth(currentDate: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        calendar.time = dateFormat.parse(currentDate)!!

        calendar.add(Calendar.MONTH, 1)

        return dateFormat.format(calendar.time)
    }

    private fun updateUI(premiumData: List<PremiumModel>) {
        val adapter = PremiumAdapter(premiumData)
        adapter.setOnButtonBuyClickListener(object : PremiumAdapter.OnButtonBuyClickListener {
            override fun onButtonBuyClick(title: String) {
                viewModel.updateProfileStatus( title, addOneMonth(getCurrentDate())).observe(this@PremiumActivity) { result ->
                    when (result) {
                        is Result.Success -> {
                            showToast("Profile updated successfully")
                            finish()
                        }
                        is Result.Error -> {
                            showToast("Error updating profile: ${result.error}")
                            println(result.error)
                        }

                        else -> {}
                    }
                }
            }
        })
        binding.rvPremiumContent.layoutManager = LinearLayoutManager(this)
        binding.rvPremiumContent.adapter = adapter
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}