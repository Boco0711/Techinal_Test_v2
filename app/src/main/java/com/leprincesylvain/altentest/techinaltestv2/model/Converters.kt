package com.leprincesylvain.altentest.techinaltestv2.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.leprincesylvain.altentest.techinaltestv2.model.user.Address

class Converters {

    var gson = Gson()

    @TypeConverter
    fun addressItemToString(address: Address): String {
        return gson.toJson(address)
    }

    @TypeConverter
    fun stringToAddress(data: String): Address {
        val listType = object : TypeToken<Address>() {
        }.type
        return gson.fromJson(data, listType)
    }
}
