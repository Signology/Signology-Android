package com.adem.signology.view.ui.register

import androidx.lifecycle.ViewModel
import com.adem.signology.data.UserRepository

class RegisterViewModel(private val usrRepo: UserRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) = usrRepo.register(name, email, password)
}