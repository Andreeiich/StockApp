package com.example.stockapp.features.home.data

import com.google.gson.annotations.SerializedName

data class StockNameDTO(
    @SerializedName("symbol")
    val companyName:String,
    @SerializedName( "changesPercentage")
    val changesPercentage:Double
    )
