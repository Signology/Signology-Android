package com.adem.signology.view.ui.profile.edit

import androidx.lifecycle.ViewModel
import com.adem.signology.data.UserRepository

class ChangePasswordViewModel(private val repository: UserRepository) : ViewModel() {

    fun updatePassword(password: String, prevPassword: String) = repository.updatePassword(password, prevPassword)

}