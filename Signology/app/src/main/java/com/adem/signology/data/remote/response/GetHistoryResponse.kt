package com.adem.signology.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetHistoryResponse(

	@field:SerializedName("history ")
	val history: List<HistoryItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class HistoryItem(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("word")
	val word: String? = null
)
