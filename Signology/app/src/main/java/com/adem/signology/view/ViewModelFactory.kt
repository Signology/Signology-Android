package com.adem.signology.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adem.signology.data.UserRepository
import com.adem.signology.di.Injection
import com.adem.signology.view.ui.login.LoginViewModel
import com.adem.signology.view.ui.register.RegisterViewModel
import com.adem.signology.view.ui.camera.CameraActivityViewModel
import com.adem.signology.view.ui.camera.PredictViewModel
import com.adem.signology.view.ui.history.DetailHistoryViewModel
import com.adem.signology.view.ui.history.HistoryViewModel
import com.adem.signology.view.ui.home.HomeViewModel
import com.adem.signology.view.ui.learn.ListLetterViewModel
import com.adem.signology.view.ui.premium.PremiumViewModel
import com.adem.signology.view.ui.profile.ProfileViewModel
import com.adem.signology.view.ui.profile.edit.ChangePasswordViewModel
import com.adem.signology.view.ui.profile.edit.EditProfileViewModel
import com.adem.signology.view.welcome.WelcomeViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                WelcomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CameraActivityViewModel::class.java) -> {
                CameraActivityViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PredictViewModel::class.java) -> {
                PredictViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PremiumViewModel::class.java) -> {
                PremiumViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailHistoryViewModel::class.java) -> {
                DetailHistoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ChangePasswordViewModel::class.java) -> {
                ChangePasswordViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ListLetterViewModel::class.java) -> {
                ListLetterViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            synchronized(ViewModelFactory::class.java) {
                INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                return INSTANCE as ViewModelFactory
            }
        }
    }

}