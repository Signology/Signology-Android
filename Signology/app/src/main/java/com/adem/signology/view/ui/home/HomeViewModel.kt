package com.adem.signology.view.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adem.signology.data.UserRepository
import com.adem.signology.data.model.FreeUserModel
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    fun getUserById() = repository.getUserById()
    fun savePointAccSession(user: FreeUserModel) {
        viewModelScope.launch {
            repository.savePointAccSession(user)
        }
    }

}