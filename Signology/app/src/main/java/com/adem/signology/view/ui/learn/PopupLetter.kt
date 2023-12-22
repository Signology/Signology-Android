package com.adem.signology.view.ui.learn

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.adem.signology.R
import com.bumptech.glide.Glide
import java.util.Locale

class PopupLetter(context: Context, private val letter: String, private val photo:String) : Dialog(context),
    TextToSpeech.OnInitListener {

    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_letter)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvLetterDetail: TextView = findViewById(R.id.tv_title_letter)
        val ivSpeechLearn: ImageView = findViewById(R.id.ivSpeechLearn)
        var ivImageLetter: ImageView = findViewById(R.id.iv_letter_image)

        val formattedString = context.getString(R.string.letter_popup, letter)
        tvLetterDetail.text = formattedString
        Glide.with(context)
            .load(photo)
            .into(ivImageLetter)

        textToSpeech = TextToSpeech(context, this)

        ivSpeechLearn.setOnClickListener {
            speakText(letter)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {

        }
    }

    private fun speakText(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun dismiss() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
        super.dismiss()
    }
}