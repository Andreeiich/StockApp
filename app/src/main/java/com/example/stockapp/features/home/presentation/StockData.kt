package com.example.stockapp.features.home.presentation

data class StockData(
    val ticker: String,
    val companyName: String,
    val currentPrice: Double,
    val dayDelta: Double,
    val image: String
)
