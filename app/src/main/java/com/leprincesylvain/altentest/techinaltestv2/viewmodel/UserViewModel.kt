package com.leprincesylvain.altentest.techinaltestv2.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.leprincesylvain.altentest.techinaltestv2.model.user.UserTableModel
import com.leprincesylvain.altentest.techinaltestv2.repository.UserRepository

class UserViewModel : ViewModel() {

    private var liveDataUser: LiveData<UserTableModel>? = null

    fun insertUser(context: Context, userTableModel: UserTableModel) {
        UserRepository.insertUser(context, userTableModel)
    }

    fun getUser(context: Context, id: Int): LiveData<UserTableModel>? {
        liveDataUser = UserRepository.getUser(context, id)
        return liveDataUser
    }

}