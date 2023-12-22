package com.adem.signology.view.ui.premium

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adem.signology.data.UserRepository
import com.adem.signology.data.model.premium.PremiumModel

class PremiumViewModel(private val repository: UserRepository) : ViewModel() {
    private val _premiumData = MutableLiveData<List<PremiumModel>>()
    val premiumData: LiveData<List<PremiumModel>> get() = _premiumData

    fun fetchPremiumData() {
        _premiumData.value = repository.getPremiumData()
    }

    fun updateProfileStatus(accType: String, premiumDate: String) = repository.updateProfileStatus(accType, premiumDate)

}