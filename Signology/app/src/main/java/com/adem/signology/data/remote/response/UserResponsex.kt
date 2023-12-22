package com.adem.signology.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponsex(

	@field:SerializedName("user ")
	val user: User? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Userx(

	@field:SerializedName("is_premium")
	val isPremium: Boolean? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("profile_pic")
	val profilePic: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("premium_date")
	val premiumDate: Any? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("point")
	val point: Int? = null,

	@field:SerializedName("username")
	val username: String? = null
)
