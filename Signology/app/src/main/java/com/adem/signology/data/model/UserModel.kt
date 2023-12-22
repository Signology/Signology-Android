package com.adem.signology.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: Int,
    var name: String? = null,
    var email: String? = null,
    var token: String? = null,
    var point:Int,
    var accType: String? = null,
    val historyId: Int,
    val isLogin: Boolean
) : Parcelable