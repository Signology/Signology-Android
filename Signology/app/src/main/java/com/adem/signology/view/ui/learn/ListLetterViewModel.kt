package com.adem.signology.view.ui.learn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adem.signology.data.UserRepository
import com.adem.signology.data.model.letter.Letter

class ListLetterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _letterData = MutableLiveData<List<Letter>>()
    val letterData: LiveData<List<Letter>> get() = _letterData

    fun fetchLetterData() {
        _letterData.value = repository.getLetterData()
    }
}