package com.adem.signology.data.model.premium

data class PremiumModel(
    val id: Long,
    val title: String,
    val price: String,
    val content: List<String>,
)