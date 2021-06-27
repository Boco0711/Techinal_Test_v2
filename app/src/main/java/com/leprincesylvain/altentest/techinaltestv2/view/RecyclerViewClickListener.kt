package com.leprincesylvain.altentest.techinaltestv2.view

import com.leprincesylvain.altentest.techinaltestv2.model.device.DeviceTableModel

interface RecyclerViewClickListener {
    fun onItemDeleteClick(device: DeviceTableModel)
    fun onItemChangeClick(device: DeviceTableModel)
}