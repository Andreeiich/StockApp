package com.example.stockapp.features.home.data

import com.example.stockapp.features.home.domain.DataRepository


class DataRepositoryImpl(
    private val api: ServerDataApi
) : DataRepository {

    override suspend fun getAllDataOfStocks(stock: String): MutableList<StockDTO> {
        return api.getAllStock(stock)
    }

    override suspend fun getAllNameOfStocks(): List<StockNameDTO> {
        return api.getNameOfStock()
    }
}
