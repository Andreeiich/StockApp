package com.example.stockapp.features.home.domain

import com.example.stockapp.features.home.data.StockDTO
import com.example.stockapp.features.home.data.StockNameDTO

interface DataRepository {
    suspend fun getAllDataOfStocks(stock: String): List<StockDTO>
    suspend fun getAllNameOfStocks(): List<StockNameDTO>
    suspend fun getDataOfStockByQuery(stock: String): List<StockNameDTO>
}
