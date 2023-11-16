package com.example.stockapp.features.home.domain

interface StockDataHelper {
    suspend fun takeStock(
        item: DataDomain
    ): DataDomain
}
