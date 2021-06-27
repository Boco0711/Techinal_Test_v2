package com.leprincesylvain.altentest.techinaltestv2.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.leprincesylvain.altentest.techinaltestv2.model.device.DeviceTableModel
import com.leprincesylvain.altentest.techinaltestv2.room.DataDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class DeviceRepository {

    companion object {

        private var dataDatabase: DataDatabase? = null

        private var deviceTableModel: LiveData<DeviceTableModel>? = null

        private fun initializeDB(context: Context): DataDatabase {
            return DataDatabase.getDatabaseInstance(context)
        }

        fun insertDevice(context: Context, deviceTableModel: DeviceTableModel) {

            dataDatabase = initializeDB(context)

            CoroutineScope(IO).launch {
                dataDatabase!!.deviceDao().insertDevice(deviceTableModel)
            }

        }

        fun insertDevices(context: Context, devices: List<DeviceTableModel>) {
            dataDatabase = initializeDB(context)

            CoroutineScope(IO).launch {
                dataDatabase!!.deviceDao().insertDevices(devices)
            }

        }

        fun getDevice(context: Context, id: Int): LiveData<DeviceTableModel>? {

            dataDatabase = initializeDB(context)

            deviceTableModel = dataDatabase!!.deviceDao().getDevice(id)

            return deviceTableModel
        }

        fun getDevices(context: Context): LiveData<List<DeviceTableModel>> {
            dataDatabase = initializeDB(context)
            return dataDatabase!!.deviceDao().getDevices()
        }

    }
}
