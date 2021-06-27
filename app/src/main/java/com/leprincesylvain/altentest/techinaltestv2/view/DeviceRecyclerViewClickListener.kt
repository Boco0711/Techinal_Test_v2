package com.leprincesylvain.altentest.techinaltestv2.view

import com.leprincesylvain.altentest.techinaltestv2.model.device.DeviceTableModel

interface DeviceRecyclerViewClickListener {
    fun onDeviceDeleteClick(device: DeviceTableModel)
    fun onDeviceChangeClick(device: DeviceTableModel)
}