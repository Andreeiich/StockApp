package com.example.stockapp.features.home.data

import com.google.gson.annotations.SerializedName

data class StockDTO(
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("changes")
    val changes: Double,
    @SerializedName("companyName")
    val companyName: String,
    @SerializedName("image")
    val image: String,
)
