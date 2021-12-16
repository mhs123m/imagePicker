package com.twq.imgpickerwarmup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel() : ViewModel() {
    val userRepository = UserRepository()
    fun getAllUsers (): LiveData<List<User>>{

        val mLiveData = MutableLiveData<List<User>>()
        userRepository.getAllUsers().observeForever {
            mLiveData.postValue(it)
        }
        return mLiveData
    }

    fun updateImg (id:String, user: User): LiveData<User>{
        val mLiveData = MutableLiveData<User>()
        userRepository.updatePicture(id, user).observeForever {
            mLiveData.postValue(it)
        }
        return mLiveData
    }

}
