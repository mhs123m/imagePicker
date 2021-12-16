package com.twq.imgpickerwarmup.network

import com.twq.imgpickerwarmup.User
import retrofit2.Call
import retrofit2.http.*

interface UserServices {

    @GET("users")
    fun getAllUsers (): Call<List<User>>



    @PATCH("users/{id}")
    fun updatePicture (@Path("id") id:String,@Body user:User ): Call<User>
}