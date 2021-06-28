package com.leprincesylvain.altentest.techinaltestv2.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.leprincesylvain.altentest.techinaltestv2.R
import com.leprincesylvain.altentest.techinaltestv2.model.device.DeviceTableModel
import com.leprincesylvain.altentest.techinaltestv2.repository.DeviceRepository
import com.leprincesylvain.altentest.techinaltestv2.viewmodel.DeviceViewModel
import kotlinx.android.synthetic.main.fragment_device_layout.*
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class FragmentDevices : Fragment(), DeviceRecyclerViewClickListener {
    private lateinit var deviceViewModel: DeviceViewModel
    private lateinit var repository: DeviceRepository
    private var adapter: DevicesAdapter? = null
    private lateinit var preferences: SharedPreferences
    private var callToReadJson = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_device_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = this.requireActivity()
            .getSharedPreferences("pref", Context.MODE_PRIVATE)
        callToReadJson = preferences.getInt("nbOfCall", 0)

        repository = DeviceRepository()
        deviceViewModel = ViewModelProvider(this).get(DeviceViewModel::class.java)
        if (callToReadJson == 0)
            readJson()
        deviceViewModel.getDevices(this.requireContext())
            .observe(viewLifecycleOwner, { devices ->
                recycler_view_device.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.setHasFixedSize(true)
                    adapter = DevicesAdapter(devices, this)
                    it.adapter = adapter
                }
            })
    }

    private fun readJson() {
        callToReadJson++
        val json: String?

        val list: MutableList<DeviceTableModel> = ArrayList()

        try {
            val inputStream: InputStream = this.requireContext().assets.open("data.json")
            json = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(json)
            val jsonArray = jsonObject.getJSONArray("devices")
            for (i in 0 until jsonArray.length()) {
                val device = jsonArray.getJSONObject(i)
                val id = device.getInt("id")
                val deviceName = device.getString("deviceName")
                val productType = device.getString("productType")
                var intensity: Int? = null
                var position: Int? = null
                var temperature: Double? = null
                var modeString: String? = null
                if (device.has("intensity")) {
                    intensity = device.getInt("intensity")
                }
                if (device.has("position")) {
                    position = device.getInt("position")
                }
                if (device.has("temperature")) {
                    temperature = device.getDouble("temperature")
                }
                if (device.has("mode")) {
                    modeString = device.getString("mode")
                }

                val deviceTableModel = DeviceTableModel(
                    id,
                    deviceName,
                    productType,
                    intensity,
                    position,
                    temperature,
                    modeString
                )
                list.add(deviceTableModel)
            }
            deviceViewModel.insertDevices(this.requireContext(), list)
        } catch (e: IOException) {
        }
    }

    override fun onDeviceDeleteClick(device: DeviceTableModel) {
        deviceViewModel.deleteDevice(requireContext(), device)
    }

    override fun onDeviceChangeClick(device: DeviceTableModel) {
        deviceViewModel.updateDevice(requireContext(), device)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val edt: SharedPreferences.Editor = preferences.edit()
        edt.putInt("nbOfCall", callToReadJson)
        edt.apply()
        super.onSaveInstanceState(outState)
    }
}