package com.adem.signology.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.adem.signology.data.model.FreeUserModel
import com.adem.signology.data.model.HistoryIdModel
import com.adem.signology.data.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = user.id
            preferences[NAME_KEY] = user.name.toString()
            preferences[EMAIL_KEY] = user.email.toString()
            preferences[TOKEN_KEY] = user.token.toString()
            preferences[ACC_TYPE] = user.accType.toString()
            preferences[USER_POINT] = user.point
            preferences[IS_LOGIN_KEY] = true
        }
    }

    suspend fun savePointAccSession(user: FreeUserModel) {
        dataStore.edit { preferences ->
            preferences[ACC_TYPE] = user.accType
            preferences[USER_POINT] = user.point
        }
    }

    suspend fun saveHistoryIdSession(data: HistoryIdModel) {
        dataStore.edit { preferences ->
            preferences[HISTORY_ID] = data.id
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                id = preferences[USER_ID] as Int? ?: 0,
                name = preferences[NAME_KEY] as String? ?: "",
                email = preferences[EMAIL_KEY] as String? ?: "",
                token= preferences[TOKEN_KEY] ?: "",
                accType = preferences[ACC_TYPE]?: "",
                point = preferences[USER_POINT] ?: 0,
                historyId = preferences[USER_ID] as Int? ?: 0,
                isLogin = preferences[IS_LOGIN_KEY] as Boolean? ?: false
            )
        }
    }

    fun getPointAccSession(): Flow<FreeUserModel> {
        return dataStore.data.map { preferences ->
            FreeUserModel(
                accType = preferences[ACC_TYPE] ?: "",
                point = preferences[USER_POINT] ?: 0,
            )
        }
    }

    fun getHistoryIdSession(): Flow<HistoryIdModel> {
        return  dataStore.data.map { preferences ->
            HistoryIdModel(
                id = preferences[HISTORY_ID] ?: 0
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val USER_ID = intPreferencesKey("id")
        private val USER_POINT = intPreferencesKey("point")
        private val ACC_TYPE = stringPreferencesKey("accType")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val HISTORY_ID = intPreferencesKey("historyId")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}