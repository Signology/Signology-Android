package com.adem.signology.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetHistoryImageResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("image_histories")
	val imageHistories: List<ImageHistoriesItem?>? = null
)

data class ImageHistoriesItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("history_id")
	val historyId: Int? = null
)
