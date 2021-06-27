package com.leprincesylvain.altentest.techinaltestv2.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.leprincesylvain.altentest.techinaltestv2.model.user.UserTableModel

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userTableModel: UserTableModel)

    @Query("SELECT * FROM User_Table WHERE id = :id")
    fun getUser(id: Int?): LiveData<UserTableModel>
}