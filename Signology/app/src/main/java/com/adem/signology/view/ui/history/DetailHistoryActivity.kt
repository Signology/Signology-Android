package com.adem.signology.view.ui.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adem.signology.R
import com.adem.signology.data.Result
import com.adem.signology.data.remote.response.ImageHistoriesItem
import com.adem.signology.databinding.ActivityDetailHistoryBinding
import com.adem.signology.view.ViewModelFactory
import java.util.Locale

class DetailHistoryActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private val viewModel by viewModels<DetailHistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var imageAdapter: DetailHistoryAdapter
    private lateinit var binding: ActivityDetailHistoryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("id", -1)
        val word = intent.getStringExtra("word")

        recyclerView = binding.rvHistoryImage
        imageAdapter = DetailHistoryAdapter(this, emptyList())
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = imageAdapter

        println("historyId: $id")

        binding.tvHistoryPredictDetail.text = word
        textToSpeech = TextToSpeech(this, this)

        binding.ivSpeechHistory.setOnClickListener {
            speakText(binding.tvHistoryPredictDetail.text.toString())
        }

        viewModel.getHistoryImage(id).observe(this) { result ->
            println("historyId: $id")

            when (result) {
                is Result.Success -> {
                    updateRecyclerView(result.data.imageHistories)
                }

                is Result.Error -> {
                    val errorMessage = result.error

                    showToast(errorMessage)
                }

                else -> {}
            }
        }
    }

    private fun updateRecyclerView(historyList: List<ImageHistoriesItem?>?) {
        imageAdapter = DetailHistoryAdapter(this, historyList)
        recyclerView.adapter = imageAdapter
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun speakText(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.US)

        }
    }
}
