package com.adem.signology.data.remote.retrofit


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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @PATCH("user")
    suspend fun updateProfile(
        @Part("username") username: RequestBody,
        @Part profilePic: MultipartBody.Part
    ): UserResponse

    @FormUrlEncoded
    @PATCH("user")
    suspend fun updateProfileStatus(
        @Field("acc_type") accType: String,
        @Field("premium_date") premiumDate: String,
    ): UserResponse

    @FormUrlEncoded
    @PATCH("user")
    suspend fun updatePoint(
        @Field("point") point: Int,
    ): UserResponse

    @FormUrlEncoded
    @PATCH("user/password")
    suspend fun updatePassword(
        @Field("password") password: String,
        @Field("prev_password") prevPassword: String
    ): UpdatePasswordResponse

    @GET("user")
    suspend fun getUserById(): UserResponse

    @FormUrlEncoded
    @POST("history")
    suspend fun history(
        @Field("word") word: String
    ): HistoryResponse

    @Multipart
    @POST("image_history")
    suspend fun imageHistory(
        @Part("history_id") historyId: Int,
        @Part image: MultipartBody.Part
    ): HistoryAddResponse

    @POST("predict/{id}")
    suspend fun predict(@Path("id") id: Int): PredictResponse

    @GET("user/history")
    suspend fun getHistory(): GetHistoryResponse

    @GET("history/{id}/image")
    suspend fun getImageHistory(@Path("id") id: Int): GetHistoryImageResponse

    @DELETE("history/{id}")
    suspend fun deleteHistory(@Path("id") id: Int): DeleteHistoryResponse

}