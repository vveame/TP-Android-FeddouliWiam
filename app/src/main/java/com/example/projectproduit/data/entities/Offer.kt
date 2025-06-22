package com.example.projectproduit.data.entities

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Offer(
    @SerializedName("offerId")
    val offerId: String,

    @SerializedName("title")
    val offerTitle: String,

    @SerializedName("description")
    val offerDescription: String? = null,

    @SerializedName("discountPercentage")
    val discountPercentage: Double,

    @SerializedName("startDate")
    val startDate: Date,

    @SerializedName("endDate")
    val endDate: Date? = null,

    @SerializedName("applicableProductIds")
    val applicableProductIds: List<String> = emptyList(),

    @SerializedName("active")
    val active: Boolean = true
)
