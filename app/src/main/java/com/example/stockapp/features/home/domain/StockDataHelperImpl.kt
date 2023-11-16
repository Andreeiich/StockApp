package com.example.stockapp.features.home.domain

class StockDataHelperImpl : StockDataHelper {
    override suspend fun takeStock(
        item: DataDomain
    ): DataDomain {
        return item
    }
}
