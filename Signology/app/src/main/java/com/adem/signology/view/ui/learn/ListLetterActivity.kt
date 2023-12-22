package com.adem.signology.view.ui.learn

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.adem.signology.R
import com.adem.signology.data.Result
import com.adem.signology.data.model.letter.Letter
import com.adem.signology.data.model.premium.PremiumModel
import com.adem.signology.databinding.ActivityListLetterBinding
import com.adem.signology.view.ViewModelFactory
import com.adem.signology.view.ui.premium.PremiumAdapter
import com.adem.signology.view.ui.premium.PremiumViewModel

class ListLetterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListLetterBinding

    private val viewModel by viewModels<ListLetterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListLetterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f

        viewModel.letterData.observe(this, Observer { data ->
            updateUI(data)
        })

        viewModel.fetchLetterData()

    }

    private fun updateUI(letterData: List<Letter>) {
        val adapter = ListLetterAdapter(letterData)
        binding.rvLetters.layoutManager = GridLayoutManager(this, 3)
        binding.rvLetters.adapter = adapter
    }

}
