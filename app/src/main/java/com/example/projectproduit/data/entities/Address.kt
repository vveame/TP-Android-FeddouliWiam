package com.example.projectproduit.data.entities

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("street")
    val street: String,

    @SerializedName("city")
    val city: String,

    @SerializedName("postal_code")
    val postalCode: String,

    @SerializedName("country")
    val country: String
)