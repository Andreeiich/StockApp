package com.example.stockapp.features.home.data

import com.google.gson.annotations.SerializedName

data class StockDTO(
@SerializedName("symbol")
var symbol: String,
@SerializedName("price")
var price: Double,
@SerializedName("changes")
var changes: Double,
@SerializedName("companyName")
var companyName: String,
@SerializedName("image")
val image: String,
)
