package com.adem.signology.view.ui.profile.edit

import androidx.lifecycle.ViewModel
import com.adem.signology.data.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditProfileViewModel(private val repository: UserRepository) : ViewModel() {

    fun editProfile(username: RequestBody, imageFile: MultipartBody.Part) = repository.updateProfile(username, imageFile)

    fun getUserById() = repository.getUserById()
}
