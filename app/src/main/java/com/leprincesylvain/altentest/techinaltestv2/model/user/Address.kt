package com.leprincesylvain.altentest.techinaltestv2.model.user

data class Address(
    val city: String,
    val country: String,
    val postalCode: Int,
    val street: String,
    val streetCode: String
)