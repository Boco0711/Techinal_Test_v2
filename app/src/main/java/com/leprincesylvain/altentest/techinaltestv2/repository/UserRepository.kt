package com.leprincesylvain.altentest.techinaltestv2.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.leprincesylvain.altentest.techinaltestv2.model.user.UserTableModel
import com.leprincesylvain.altentest.techinaltestv2.room.DataDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository {
    companion object {
        private var dataDatabase: DataDatabase? = null

        private fun initializeDB(context: Context): DataDatabase {
            return DataDatabase.getDatabaseInstance(context)
        }

        fun insertUser(context: Context, usertableModel: UserTableModel) {
            dataDatabase = initializeDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                dataDatabase!!.userDao().insertUser(usertableModel)
            }
        }

        fun getUser(context: Context): LiveData<UserTableModel> {
            dataDatabase = initializeDB(context)
            return dataDatabase!!.userDao().getUser()
        }

        fun updateUser(context: Context, usertableModel: UserTableModel) {
            dataDatabase = initializeDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                dataDatabase!!.userDao().updateUser(usertableModel)
            }
        }
    }
}