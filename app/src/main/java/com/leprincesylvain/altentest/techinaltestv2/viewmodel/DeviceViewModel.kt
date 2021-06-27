package com.leprincesylvain.altentest.techinaltestv2.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.leprincesylvain.altentest.techinaltestv2.model.device.DeviceTableModel
import com.leprincesylvain.altentest.techinaltestv2.repository.DeviceRepository

class DeviceViewModel : ViewModel() {

    private var liveDataDevice: LiveData<DeviceTableModel>? = null

    fun insertDevice(context: Context, deviceTableModel: DeviceTableModel) {
        DeviceRepository.insertDevice(context, deviceTableModel)
    }

    fun insertDevices(context: Context, devices: List<DeviceTableModel>) {
        DeviceRepository.insertDevices(context, devices)
    }

    fun getDevice(context: Context, id: Int): LiveData<DeviceTableModel>? {
        liveDataDevice = DeviceRepository.getDevice(context, id)
        return liveDataDevice
    }

    fun getDevices(context: Context): LiveData<MutableList<DeviceTableModel>> {
        return DeviceRepository.getDevices(context)
    }

    fun deleteDevice(context: Context, device: DeviceTableModel) {
        DeviceRepository.deleteDevice(context, device)
    }

}
