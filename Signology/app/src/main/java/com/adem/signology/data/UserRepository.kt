package com.adem.signology.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.adem.signology.data.model.FreeUserModel
import com.adem.signology.data.model.HistoryIdModel
import com.adem.signology.data.model.UserModel
import com.adem.signology.data.model.letter.Letter
import com.adem.signology.data.model.letter.LetterData
import com.adem.signology.data.model.premium.CardPremiumData
import com.adem.signology.data.model.premium.PremiumModel
import com.adem.signology.data.pref.UserPreference
import com.adem.signology.data.remote.response.DeleteHistoryResponse
import com.adem.signology.data.remote.response.GetHistoryImageResponse
import com.adem.signology.data.remote.response.GetHistoryResponse
import com.adem.signology.data.remote.response.HistoryAddResponse
import com.adem.signology.data.remote.response.HistoryResponse
import com.adem.signology.data.remote.response.LoginResponse
import com.adem.signology.data.remote.response.PredictResponse
import com.adem.signology.data.remote.response.RegisterResponse
import com.adem.signology.data.remote.response.UpdatePasswordResponse
import com.adem.signology.data.remote.response.UserResponse
import com.adem.signology.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val pref: UserPreference,
    private val apiService: ApiService,
//    private val storyDao: StoryDao,
//    private val storyDatabase: StoryDatabase
    ){

    fun register(name: String, email: String, password: String) : LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try{
            val result = apiService.register(name, email, password)
            emit(Result.Success(result))
        }catch (e : Exception)
        {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(email: String, password: String) : LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.login(email, password)
            emit(Result.Success(result))
        }catch (e: Exception)
        {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun updateProfile(
        username: RequestBody,
        imageFile: MultipartBody.Part,

        ): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.updateProfile(
                username,
                imageFile,
            )
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun updateProfileStatus (
        accType: String,
        premiumDate: String,

        ): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.updateProfileStatus(
                accType,
                premiumDate,
            )
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }


    fun updatePoint (
        point: Int,
        ): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.updatePoint(point)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun updatePassword (
        password: String,
        prevPassword: String,
    ): LiveData<Result<UpdatePasswordResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.updatePassword(password, prevPassword)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }



    fun getUserById(): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.getUserById()
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun history(word: String): LiveData<Result<HistoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.history(
                word,
            )
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun historyAdd(historyId: Int, image: MultipartBody.Part): LiveData<Result<HistoryAddResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.imageHistory(
                historyId,
                image,
            )
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getPredict(historyId: Int): LiveData<Result<PredictResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.predict(historyId)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getHistory(): LiveData<Result<GetHistoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.getHistory()
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getHistoryImage(historyId: Int): LiveData<Result<GetHistoryImageResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.getImageHistory(historyId)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun deleteHistory(historyId: Int): LiveData<Result<DeleteHistoryResponse>> = liveData {
        emit(Result.Loading)

        try {
            val result = apiService.deleteHistory(historyId)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getPremiumData(): List<PremiumModel> {
        return CardPremiumData.premiumData
    }

    fun getLetterData(): List<Letter> {
        return LetterData.letterData
    }

    suspend fun saveSession(user: UserModel) {
        pref.saveSession(user)
    }

    suspend fun savePointAccSession(freeUser: FreeUserModel) {
        pref.savePointAccSession(freeUser)
    }

    suspend fun saveHistoryIdSession(data: HistoryIdModel) {
        pref.saveHistoryIdSession(data)
    }

    fun getSession(): Flow<UserModel> {
        return pref.getSession()
    }

    fun getPointAccSession(): Flow<FreeUserModel> {
        return pref.getPointAccSession()
    }

    fun  getHistoryIdSession(): Flow<HistoryIdModel> {
        return pref.getHistoryIdSession()
    }

    suspend fun logout() {
        pref.logout()
//        storyDao.deleteAll()
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null
        fun getInstance(
            preference: UserPreference,
            apiService: ApiService,
//            storyDao: StoryDao,
//            storyDatabase: StoryDatabase
        ): UserRepository {
            synchronized(this) {
                val instance = UserRepository(preference,apiService)
                INSTANCE = instance
                return instance
            }
        }
    }
}