package com.leprincesylvain.altentest.techinaltestv2.model.device

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices_table")
data class DeviceTableModel(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "device_name")
    var deviceName: String,

    @ColumnInfo(name = "product_type")
    var productType: String,

    @ColumnInfo(name = "intensity")
    var intensity: Int?,

    @ColumnInfo(name = "position")
    var position: Int?,

    @ColumnInfo(name = "temperature")
    var temperature: Double?,

    @ColumnInfo(name = "mode")
    var mode: String?
)