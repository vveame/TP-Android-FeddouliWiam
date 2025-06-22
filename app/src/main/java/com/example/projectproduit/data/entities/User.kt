package com.example.projectproduit.data.entities

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("userId")
    val userId: String,

    @SerializedName("fullName")
    val fullName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,

    @SerializedName("address")
    val address: Address? = null,

    @SerializedName("createdAt")
    val createdAt: Long = System.currentTimeMillis(),

    @SerializedName("isVerified")
    val isVerified: Boolean = false
)
