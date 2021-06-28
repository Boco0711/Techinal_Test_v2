package com.leprincesylvain.altentest.techinaltestv2.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.leprincesylvain.altentest.techinaltestv2.R
import com.leprincesylvain.altentest.techinaltestv2.model.user.Address
import com.leprincesylvain.altentest.techinaltestv2.model.user.UserTableModel
import com.leprincesylvain.altentest.techinaltestv2.repository.UserRepository
import com.leprincesylvain.altentest.techinaltestv2.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_profile_layout.*
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class FragmentUserProfile : Fragment() {
    private lateinit var repository: UserRepository
    private lateinit var userViewModel: UserViewModel
    private lateinit var preferences: SharedPreferences
    private lateinit var user: UserTableModel
    private var callToReadJson = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = this.requireActivity()
            .getSharedPreferences("pref", Context.MODE_PRIVATE)
        callToReadJson = preferences.getInt("userDataCall", 0)

        repository = UserRepository()
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        if (callToReadJson == 0) {
            println("Data call json")
            readJson()
        }
        userViewModel.getUser(this.requireContext()).observe(viewLifecycleOwner, Observer {
            it.let {
                user = it
                user_firstname_text.setText(it.firstName)
                user_lastname_text.setText(it.lastName)
                user_city_text.setText(it.address.city)
                user_postalcode_text.setText(it.address.postalCode.toString())
                user_street_text.setText(it.address.street)
                user_streetcode_text.setText(it.address.streetCode)
                user_country_text.setText(it.address.country)
                user_birthdate_text.setText(getDate(it.birthDate, "dd/MM/yyyy"))
            }
        })

        var i = 0
        profile_edit_button.setOnClickListener {
            i++
            i %= 2
            if (i == 0) {
                user_firstname_text.isEnabled = false
                user.firstName = user_firstname_text.text.toString()
                user_lastname_text.isEnabled = false
                user.lastName = user_lastname_text.text.toString()
                user_city_text.isEnabled = false
                user.address.city = user_city_text.text.toString()
                user_postalcode_text.isEnabled = false
// TODO handle error case                user.address.postalCode = Integer.parseInt(user_postalcode_text.text.toString())
                user_street_text.isEnabled = false
                user.address.street = user_street_text.text.toString()
                user_streetcode_text.isEnabled = false
                user.address.streetCode = user_streetcode_text.text.toString()
                user_country_text.isEnabled = false
                user.address.country = user_country_text.text.toString()
                user_birthdate_text.isEnabled = false
                // TODO handle error case  user.birthDate = user_birthdate_text.date
                profile_edit_button.setText("Edit profile")
                userViewModel.updateUser(this.requireContext(), user)
            } else {
                user_firstname_text.isEnabled = true
                user_lastname_text.isEnabled = true
                user_city_text.isEnabled = true
                user_postalcode_text.isEnabled = true
                user_street_text.isEnabled = true
                user_streetcode_text.isEnabled = true
                user_country_text.isEnabled = true
                user_birthdate_text.isEnabled = true
                profile_edit_button.setText("Save Change")
            }
        }
    }

    fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds)
        return formatter.format(calendar.getTime())
    }

    private fun readJson() {
        callToReadJson++
        val json: String?
        try {
            val inputStream: InputStream = this.requireContext().assets.open("data.json")
            json = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(json)
            val user = jsonObject.getJSONObject("user")
            val firstName = user.getString("firstName")
            val lastName = user.getString("lastName")
            val address = user.getJSONObject("address")
            val city = address.getString("city")
            val postalCode = address.getInt("postalCode")
            val street = address.getString("street")
            val streetCode = address.getString("streetCode")
            val country = address.getString("country")
            val birthDate = user.getLong("birthDate")

            val address_ = Address(city, country, postalCode, street, streetCode)
            val userTableModel = UserTableModel(1, address_, birthDate, firstName, lastName)
            userViewModel.insertUser(this.requireContext(), userTableModel)
        } catch (e: IOException) {
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val edt: SharedPreferences.Editor = preferences.edit()
        edt.putInt("userDataCall", callToReadJson)
        edt.apply()
        super.onSaveInstanceState(outState)
    }
}