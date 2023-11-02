package com.example.stockapp.features.home.domain

import com.example.stockapp.features.home.data.StockDTO

interface StockDataHelper {
    suspend fun takeStock(
        stockDTOList: List<StockDTO>
    ): DataDomain
}
