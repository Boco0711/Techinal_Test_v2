package com.leprincesylvain.altentest.techinaltestv2.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
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

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FragmentUserProfile : Fragment() {
    private lateinit var repository: UserRepository
    private lateinit var userViewModel: UserViewModel
    private lateinit var preferences: SharedPreferences
    private lateinit var user: UserTableModel
    private var callToReadJson = 0
    private var i = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
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
        userViewModel.getUser(this.requireContext()).observe(viewLifecycleOwner, {
            it.let {
                user = it
                user_firstname_text.setText(it.firstName)
                user_lastname_text.setText(it.lastName)
                user_city_text.setText(it.address.city)
                user_postalcode_text.setText(it.address.postalCode.toString())
                user_street_text.setText(it.address.street)
                user_streetcode_text.setText(it.address.streetCode)
                user_country_text.setText(it.address.country)
                user_birthdate_text.setText(getDateFromMilliSeconds(it.birthDate, "dd/MM/yyyy"))
            }
        })

        listenToCancel()
        profile_edit_button.setOnClickListener {
            i++
            i %= 2
            if (i == 0) {
                when {
                    checkIfFieldsAreEmpty() -> {
                        Toast.makeText(
                            requireContext(),
                            "One or more field are empty, please fill them in or click on cancel",
                            Toast.LENGTH_LONG
                        ).show()
                        i++
                    }
                    checkIfPostalCodeValueIsWrong() -> {
                        Toast.makeText(
                            requireContext(),
                            "PostalCode must be 5 Digit",
                            Toast.LENGTH_LONG
                        ).show()
                        i++
                    }
                    checkIfBirthDateValueIsWrong() -> {
                        Toast.makeText(
                            requireContext(),
                            "Invalid birthdate value",
                            Toast.LENGTH_LONG
                        ).show()
                        i++
                    }
                    else -> {
                        changeCancelVisibility(false)
                        changeEditText(false)
                        updateUser()
                        profile_edit_button.text = getString(R.string.edit_profile)
                    }
                }
            } else {
                changeCancelVisibility(true)
                changeEditText(true)
                profile_edit_button.text = getString(R.string.save_change)
            }
        }
    }

    private fun checkIfBirthDateValueIsWrong(): Boolean {
        val birthYear =
            Integer.parseInt(user_birthdate_text.text.toString().substring(6))
        if (user_birthdate_text.text.toString().length != 10) {
            return true
        } else if (user_birthdate_text.text.toString()[2] != '/' || user_birthdate_text.text.toString()[5] != '/') {
            return true
        } else if (user_birthdate_text.text.toString().replace("/", "").length != 8)
            return true
        else if (birthYear < 1903 || birthYear > 2020)
            return true
        return false
    }

    private fun checkIfPostalCodeValueIsWrong(): Boolean {
        return user_postalcode_text.text.toString().length != 5
    }

    private fun listenToCancel() {
        profile_cancel_button.setOnClickListener {
            i++
            changeCancelVisibility(false)
            changeEditText(false)
            profile_edit_button.text = getString(R.string.edit_profile)
            userViewModel.updateUser(this.requireContext(), user)
            Toast.makeText(
                requireContext(),
                "Edit profile canceled",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateUser() {
        user.firstName = user_firstname_text.text.toString()
        user.lastName = user_lastname_text.text.toString()
        user.address.city = user_city_text.text.toString()
        user.address.postalCode = (user_postalcode_text.text.toString()).toLong()
        user.address.street = user_street_text.text.toString()
        user.address.streetCode = user_streetcode_text.text.toString()
        user.address.country = user_country_text.text.toString()
        user.birthDate = getMilliSecondsFromDate(user_birthdate_text.text.toString())
        userViewModel.updateUser(this.requireContext(), user)
    }

    private fun changeEditText(b: Boolean) {
        user_firstname_text.isEnabled = b
        user_lastname_text.isEnabled = b
        user_city_text.isEnabled = b
        user_postalcode_text.isEnabled = b
        user_street_text.isEnabled = b
        user_streetcode_text.isEnabled = b
        user_country_text.isEnabled = b
        user_birthdate_text.isEnabled = b
    }

    private fun changeCancelVisibility(boolean: Boolean) {
        if (boolean)
            profile_cancel_button.visibility = View.VISIBLE
        else
            profile_cancel_button.visibility = View.GONE

    }

    private fun checkIfFieldsAreEmpty(): Boolean {
        when {
            TextUtils.isEmpty(user_firstname_text.text) -> {
                user_firstname_text.error = "You must declare a firstName"

                return true
            }
            TextUtils.isEmpty(user_lastname_text.text) -> {
                user_lastname_text.error = "You must declare a lastName"
                return true
            }
            TextUtils.isEmpty(user_city_text.text) -> {
                user_city_text.error = "You must declare a city"
                return true
            }
            TextUtils.isEmpty(user_postalcode_text.text) -> {
                user_postalcode_text.error = "You must declare a postalCode"
                return true
            }
            TextUtils.isEmpty(user_street_text.text) -> {
                user_street_text.error = "You must declare a street"
                return true
            }
            TextUtils.isEmpty(user_streetcode_text.text) -> {
                user_streetcode_text.error = "You must declare a streetCode"
                return true
            }
            TextUtils.isEmpty(user_country_text.text) -> {
                user_country_text.error = "You must declare a country"
                return true
            }
            TextUtils.isEmpty(user_birthdate_text.text) -> {
                user_birthdate_text.error = "You must declare a birthDate"
                return true
            }
        }
        return false
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateFromMilliSeconds(milliSeconds: Long, dateFormat: String): String {
        val formatter = SimpleDateFormat(dateFormat)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getMilliSecondsFromDate(string: String): Long {
        val parser = SimpleDateFormat("d/MM/yyyy")
        val date = parser.parse(string)
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        formatter.timeZone = TimeZone.getTimeZone("CEST")
        val formattedDate = formatter.format(date!!)
        return formatter.parse(formattedDate).time
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
            val postalCode = address.getLong("postalCode")
            val street = address.getString("street")
            val streetCode = address.getString("streetCode")
            val country = address.getString("country")
            val birthDate = user.getLong("birthDate")

            val userAddress = Address(city, country, postalCode, street, streetCode)
            val userTableModel = UserTableModel(1, userAddress, birthDate, firstName, lastName)
            userViewModel.insertUser(this.requireContext(), userTableModel)
        } catch (e: IOException) {
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_list -> {
                Navigation.findNavController(this.requireView())
                    .navigate(R.id.action_profileFragment_to_devicesFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val edt: SharedPreferences.Editor = preferences.edit()
        edt.putInt("userDataCall", callToReadJson)
        edt.apply()
        super.onSaveInstanceState(outState)
    }
}