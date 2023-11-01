package com.example.stockapp.features.home.domain

import com.example.stockapp.features.home.data.StockDTO
import com.example.stockapp.features.home.presentation.StockData

data class DataDomain(
    val stockDTOList: List<StockDTO>
) {
    fun toUI() = StockData(stockDTOList)
}
