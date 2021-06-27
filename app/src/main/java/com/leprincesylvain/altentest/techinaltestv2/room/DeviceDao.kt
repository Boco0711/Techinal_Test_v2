package com.leprincesylvain.altentest.techinaltestv2.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.leprincesylvain.altentest.techinaltestv2.model.device.DeviceTableModel

@Dao
interface DeviceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(deviceTableModel: DeviceTableModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevices(devices: List<DeviceTableModel>)

    @Query("SELECT * FROM devices_table WHERE id = :id")
    fun getDevice(id: Int?): LiveData<DeviceTableModel>

    @Query("SELECT * FROM devices_table")
    fun getDevices(): LiveData<List<DeviceTableModel>>
}