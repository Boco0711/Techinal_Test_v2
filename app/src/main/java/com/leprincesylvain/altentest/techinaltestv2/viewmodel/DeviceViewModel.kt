package com.leprincesylvain.altentest.techinaltestv2.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.leprincesylvain.altentest.techinaltestv2.model.device.DeviceTableModel
import com.leprincesylvain.altentest.techinaltestv2.repository.DeviceRepository

class DeviceViewModel : ViewModel() {

    private var liveDataDevice: LiveData<DeviceTableModel>? = null

    fun initializeDb(context: Context) {
        DeviceRepository.initializeDB(context)
    }

    fun insertDevice(context: Context, deviceTableModel: DeviceTableModel) {
        DeviceRepository.insertDevice(context, deviceTableModel)
    }

    fun insertDevices(context: Context, devices: List<DeviceTableModel>) {
        DeviceRepository.insertDevices(context, devices)
    }

    fun getDevicesByType(context: Context, type: String): LiveData<MutableList<DeviceTableModel>> {
        return  DeviceRepository.getDevicesByType(context, type)
    }

    fun getDevices(context: Context): LiveData<MutableList<DeviceTableModel>> {
        return DeviceRepository.getDevices(context)
    }

    fun deleteDevice(context: Context, device: DeviceTableModel) {
        DeviceRepository.deleteDevice(context, device)
    }

    fun updateDevice(context: Context, device: DeviceTableModel) {
        DeviceRepository.updateDevice(context, device)
    }
    
}
