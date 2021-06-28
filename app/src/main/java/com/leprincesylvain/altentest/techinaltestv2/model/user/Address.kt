package com.leprincesylvain.altentest.techinaltestv2.model.user

data class Address(
    var city: String,
    var country: String,
    var postalCode: Long,
    var street: String,
    var streetCode: String
)