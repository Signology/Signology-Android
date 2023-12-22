package com.adem.signology.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryIdModel(
    val id: Int,
) : Parcelable