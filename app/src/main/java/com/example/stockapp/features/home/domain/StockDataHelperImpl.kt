package com.example.stockapp.features.home.domain

import com.example.stockapp.features.home.data.StockDTO
import com.example.stockapp.features.home.data.StockNameDTO

class StockDataHelperImpl : StockDataHelper {
    override suspend fun takeStock(
        stockDTOList: List<StockDTO>
    ): DataDomain {
        return DataDomain(stockDTOList)
    }
}
