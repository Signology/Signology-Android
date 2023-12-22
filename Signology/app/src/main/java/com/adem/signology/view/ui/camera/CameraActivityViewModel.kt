package com.adem.signology.view.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.adem.signology.data.UserRepository
import com.adem.signology.data.model.FreeUserModel
import okhttp3.MultipartBody

class CameraActivityViewModel(private val repository: UserRepository) : ViewModel() {
    fun historyAdd(id: Int, imageFile: MultipartBody.Part) = repository.historyAdd(id, imageFile)

    fun history(word: String) = repository.history(word)
    fun deleteHistory(id: Int) = repository.deleteHistory(id)
    fun getSession(): LiveData<FreeUserModel> {
        return repository.getPointAccSession().asLiveData()
    }
}