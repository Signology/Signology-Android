package com.adem.signology.data.model.letter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Letter(
    val letter: String,
    val photo: String
) : Parcelable