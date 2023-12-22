package com.adem.signology.view.ui.history

import androidx.lifecycle.ViewModel
import com.adem.signology.data.UserRepository

class DetailHistoryViewModel(private val usrRepo: UserRepository) : ViewModel() {

    fun getHistoryImage(id: Int) = usrRepo.getHistoryImage(id)
}