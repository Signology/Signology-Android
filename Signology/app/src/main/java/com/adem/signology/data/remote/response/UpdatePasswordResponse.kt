package com.adem.signology.data.remote.response

import com.google.gson.annotations.SerializedName

data class UpdatePasswordResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
