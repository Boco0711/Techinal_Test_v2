package com.leprincesylvain.altentest.techinaltestv2.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leprincesylvain.altentest.techinaltestv2.model.device.DeviceTableModel

@Dao
interface DeviceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDevice(deviceTableModel: DeviceTableModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDevices(devices: List<DeviceTableModel>)

    @Query("SELECT * FROM devices_table WHERE id = :id")
    fun getDevice(id: Int?): LiveData<DeviceTableModel>

    @Query("SELECT * FROM devices_table")
    fun getDevices(): LiveData<MutableList<DeviceTableModel>>

    @Delete
    suspend fun deleteDevice(device: DeviceTableModel?)

    @Update
    suspend fun updateDevice(device: DeviceTableModel?)

}