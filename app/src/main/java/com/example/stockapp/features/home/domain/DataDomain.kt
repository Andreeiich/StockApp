package com.example.stockapp.features.home.domain

import com.example.stockapp.features.home.presentation.StockData

data class DataDomain(
    val ticker: String,
    val companyName: String,
    val currentPrice: Double,
    val dayDelta: Double,
    val image: String,
    val defaultImage: Boolean,
    val isActivelyTrading: Boolean
) {
    fun toUI() = StockData(ticker, companyName, currentPrice, dayDelta, image)
}
