package com.example.stockapp.features.home.data

import com.google.gson.annotations.SerializedName

data class StockNameDTO(
    @SerializedName("symbol")
    val companyName: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("changesPercentage")
    val changesPercentage: Double,
    @SerializedName("change")
    val change: Double,
    @SerializedName("dayLow")
    val dayLow: Double,
    @SerializedName("dayHigh")
    val dayHigh: Double,
    @SerializedName("yearHigh")
    val yearHigh: Double,
    @SerializedName("yearLow")
    val yearLow: Double,
    @SerializedName("marketCap")
    val marketCap: Double,
    @SerializedName("priceAvg50")
    val priceAvg50: Double,
    @SerializedName("priceAvg200")
    val priceAvg200: Double,
    @SerializedName("exchange")
    val exchange: String,
    @SerializedName("volume")
    val volume: Double,
    @SerializedName("avgVolume")
    val avgVolume: Double,
    @SerializedName("open")
    val open: Any? = null,
    @SerializedName("previousClose")
    val previousClose: Double,
    @SerializedName("eps")
    val eps: Double,
    @SerializedName("pe")
    val pe: Any? = null,
    @SerializedName("earningsAnnouncement")
    val earningsAnnouncement: Any? = null,
    @SerializedName("sharesOutstanding")
    val sharesOutstanding: Double,
    @SerializedName("timestamp")
    val timestamp: Double
)
