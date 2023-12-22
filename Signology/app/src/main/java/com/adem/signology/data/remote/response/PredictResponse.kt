package com.adem.signology.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("word_cnn")
	val wordCnn: String? = null
)
