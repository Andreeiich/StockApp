package com.example.stockapp.features.home.domain

import com.example.stockapp.features.home.data.StockDTO
import com.example.stockapp.features.home.data.StockNameDTO

interface StockDataHelper {
    suspend fun takeStock(
        stockNameDTOList: List<StockNameDTO>,
        stockDTOList: List<StockDTO>
    ): DataDomain
}
