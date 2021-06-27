package com.leprincesylvain.altentest.techinaltestv2.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leprincesylvain.altentest.techinaltestv2.model.user.UserTableModel

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userTableModel: UserTableModel)

    @Query("SELECT * FROM User_Table WHERE id = :id")
    fun getUser(id: Int?): LiveData<UserTableModel>

    @Update
    suspend fun updateUser(userTableModel: UserTableModel?)

}