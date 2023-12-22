package com.adem.signology.view.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.adem.signology.data.UserRepository
import com.adem.signology.data.model.FreeUserModel
import com.adem.signology.data.model.UserModel

class PredictViewModel(private val repository: UserRepository) : ViewModel() {

    fun predict(historyId: Int) = repository.getPredict(historyId)
    fun updatePoint(point: Int) = repository.updatePoint(point)
    fun getSession(): LiveData<FreeUserModel> {
        return repository.getPointAccSession().asLiveData()
    }
}