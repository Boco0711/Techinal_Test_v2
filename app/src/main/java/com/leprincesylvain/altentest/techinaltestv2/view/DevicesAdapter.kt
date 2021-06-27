package com.leprincesylvain.altentest.techinaltestv2.view

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.leprincesylvain.altentest.techinaltestv2.R
import com.leprincesylvain.altentest.techinaltestv2.databinding.RecyclerviewDeviceLayoutBinding
import com.leprincesylvain.altentest.techinaltestv2.model.device.DeviceTableModel
import kotlinx.android.synthetic.main.recyclerview_device_layout.view.*

class DevicesAdapter(
    private val devices: MutableList<DeviceTableModel>,
    private val listener: DeviceRecyclerViewClickListener
) :
    Adapter<DevicesAdapter.DeviceViewHolder>() {

    var deviceFilterList = ArrayList<DeviceTableModel>()

    init {
        deviceFilterList = devices as ArrayList<DeviceTableModel>
    }

    override fun getItemCount(): Int = deviceFilterList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DeviceViewHolder(
        DataBindingUtil.inflate<RecyclerviewDeviceLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_device_layout,
            parent,
            false
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.recyclerviewDeviceBinding.device = deviceFilterList[position]
        setSwitchButton(deviceFilterList[position], holder)
        setSeekBarValue(deviceFilterList[position], holder)
        holder.recyclerviewDeviceBinding.buttonDelete.setOnClickListener {
            listener.onDeviceDeleteClick(deviceFilterList[position])
            notifyDataSetChanged()
        }
    }

    private fun setSwitchButton(device: DeviceTableModel, holder: DevicesAdapter.DeviceViewHolder) {
        if (device.mode == null) {
            holder.recyclerviewDeviceBinding.deviceModeSwitch.visibility = View.GONE
        } else {
            holder.recyclerviewDeviceBinding.deviceModeSwitch.visibility = View.VISIBLE
            holder.recyclerviewDeviceBinding.deviceModeSwitch.isChecked = device.mode == "ON"
            holder.recyclerviewDeviceBinding.deviceModeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    device.mode = "ON"
                    buttonView.device_mode_switch.isChecked = true
                } else {
                    device.mode = "OFF"
                    buttonView.device_mode_switch.isChecked = false
                }
                listener.onDeviceChangeClick(device)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setSeekBarValue(device: DeviceTableModel, holder: DeviceViewHolder) {
        when (device.productType) {
            "Light" -> {
                holder.recyclerviewDeviceBinding.seekBarTextview.text = device.intensity.toString()
                holder.recyclerviewDeviceBinding.deviceSeekbar.max = 100
                holder.recyclerviewDeviceBinding.deviceSeekbar.min = 0
                holder.recyclerviewDeviceBinding.deviceSeekbar.progress = device.intensity!!
            }
            "RollerShutter" -> {
                holder.recyclerviewDeviceBinding.seekBarTextview.text = device.position.toString()
                holder.recyclerviewDeviceBinding.deviceSeekbar.max = 100
                holder.recyclerviewDeviceBinding.deviceSeekbar.min = 0
                holder.recyclerviewDeviceBinding.deviceSeekbar.progress = device.position!!
            }
            "Heater" -> {
                holder.recyclerviewDeviceBinding.seekBarTextview.text =
                    device.temperature.toString()
                holder.recyclerviewDeviceBinding.deviceSeekbar.max = 28
                holder.recyclerviewDeviceBinding.deviceSeekbar.min = 7
                holder.recyclerviewDeviceBinding.deviceSeekbar.progress = device.temperature!!
            }
        }
        holder.recyclerviewDeviceBinding.deviceSeekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                holder.recyclerviewDeviceBinding.seekBarTextview.text = progress.toString()
                when {
                    device.temperature != null -> device.temperature = progress
                    device.intensity != null -> device.intensity = progress
                    device.position != null -> device.position = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                listener.onDeviceChangeClick(device)
            }
        })
    }

    class DeviceViewHolder(
        val recyclerviewDeviceBinding: RecyclerviewDeviceLayoutBinding
    ) : RecyclerView.ViewHolder(recyclerviewDeviceBinding.root)
}