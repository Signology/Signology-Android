package com.adem.signology.di

import android.content.Context
import com.adem.signology.data.UserRepository
import com.adem.signology.data.pref.UserPreference
import com.adem.signology.data.pref.dataStore
import com.adem.signology.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token.toString())
//        val database = StoryDatabase.getInstance(context)
//        val dataStoryDao = database.storyDao()
        return UserRepository.getInstance(pref, apiService)
    }
}