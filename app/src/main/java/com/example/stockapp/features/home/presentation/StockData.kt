package com.example.stockapp.features.home.presentation

import com.example.stockapp.features.home.data.StockDTO

data class StockData(
    val ticker: String,
    val companyName: String,
    val currentPrice: Double,
    val dayDelta: Double,
    val image: String
)
