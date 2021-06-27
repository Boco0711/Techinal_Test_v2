package com.leprincesylvain.altentest.techinaltestv2.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.leprincesylvain.altentest.techinaltestv2.model.Converters

@Entity(tableName = "User_Table")
data class UserTableModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @TypeConverters(Converters::class)
    val address: Address,

    val birthDate: Long,
    val firstName: String,
    val lastName: String
)