package com.twq.imgpickerwarmup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.twq.imgpickerwarmup.network.Api
import com.twq.imgpickerwarmup.network.UserServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    fun getAllUsers (): LiveData<List<User>>{
        val mLiveData = MutableLiveData<List<User>>()
        val userServices = Api.getInstance().create(UserServices::class.java)

        userServices.getAllUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                mLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d("FAILED_REPO","${t.message}")
            }

        })

        return mLiveData

    }

    fun updatePicture (id:String,user: User): LiveData<User>{
        val mLiveData = MutableLiveData<User>()
        val userServices = Api.getInstance().create(UserServices::class.java)
        userServices.updatePicture(id,user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.d("PICTURE_ENCODED", "response: ${response.body()}")
                mLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("PICTURE_ENCODED","hello ${t.message}")
            }

        })

        return mLiveData
    }
}