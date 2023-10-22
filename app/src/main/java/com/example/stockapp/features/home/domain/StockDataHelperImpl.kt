package com.example.stockapp.features.home.domain

import com.example.stockapp.features.home.data.StockDTO
import com.example.stockapp.features.home.data.StockNameDTO

class StockDataHelperImpl : StockDataHelper {
    override suspend fun takeStock(
        stockNameDTOList: List<StockNameDTO>,
        stockDTOList: MutableList<StockDTO>
    ): DataDomain {
        return DataDomain(stockNameDTOList, stockDTOList)
    }
}
