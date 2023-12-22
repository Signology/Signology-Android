package com.adem.signology.view.ui.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.adem.signology.R
import com.adem.signology.data.Result
import com.adem.signology.databinding.ActivityPredictBinding
import com.adem.signology.view.RootActivity
import com.adem.signology.view.ViewModelFactory
import com.adem.signology.view.ui.camera.CameraActivity.Companion.EXTRA_CAMERAX_IMAGE
import java.util.Locale

class PredictActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityPredictBinding
    private val viewModel by viewModels<PredictViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var textToSpeech: TextToSpeech
    private var predict:Boolean = false
    private var point: Int = 0
    private var accType:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uId = intent.getIntExtra(EXTRA_CAMERAX_IMAGE, -1)

        supportActionBar?.setBackgroundDrawable(
            ContextCompat.getDrawable(this, R.color.white)
        )
        supportActionBar?.elevation = 0f

        textToSpeech = TextToSpeech(this, this)

        binding.ivSpeechPredict.setOnClickListener {
            speakText(binding.tvPredict.text.toString())
        }
        viewModel.getSession().observe(this) { user ->
            point = user.point - 75
            println("Point: $point")
            accType = user.accType
            println("Account type: $accType")
        }

        viewModel.predict(uId).observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvPredict.text = result.data.wordCnn
                    predict = true

                    if (predict && accType == "Free") {
                        viewModel.updatePoint(point).observe(this) { updateResult ->
                            // Handle the result directly here
                            when (updateResult) {
                                is Result.Success -> {

                                }
                                is Result.Error -> {
                                    showToast("Error updating profile: ${updateResult.error}")
                                    println(updateResult.error)
                                }
                                // Handle other cases if needed
                                else -> {}
                            }
                        }
                    }
                }

                is Result.Loading -> {
                    // Show loading indicator if needed
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    // Handle error result
                    val errorMessage = result.error
                    showToast(errorMessage)
                }

                else -> {}
            }
        }



    }
    override fun onDestroy() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
        super.onDestroy()
    }

    private fun speakText(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Language data is missing or language is not supported
                // Handle accordingly, e.g., show an error message
            }
        } else {
            // Initialization failed
            // Handle accordingly, e.g., show an error message
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}