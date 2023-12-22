package com.adem.signology.view.ui.history

import androidx.lifecycle.ViewModel
import com.adem.signology.data.UserRepository

class HistoryViewModel(private val usrRepo: UserRepository) : ViewModel() {

    fun getHistory() = usrRepo.getHistory()
}