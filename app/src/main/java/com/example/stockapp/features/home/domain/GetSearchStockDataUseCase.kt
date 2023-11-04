package com.example.stockapp.features.home.domain

import com.example.stockapp.core.domain.CoroutinesUseCase
import com.example.stockapp.features.home.presentation.StockData
import javax.inject.Inject

class GetSearchStockDataUseCase @Inject constructor(
    private val repository: DataRepository,
    private val stockHelper: StockDataHelper
) : CoroutinesUseCase<String, StockData> {

    override suspend fun invoke(params: String): StockData? {
        val searchData = repository.getDataOfStockByQuery(params)
        val companyName = searchData.map { it.companyName }
        val nameOfCompanies = companyName.toString().replace(" ", "")

        if (nameOfCompanies.isEmpty()) {
            return null
        }

        val currentData =
            repository.getAllDataOfStocks(nameOfCompanies.substring(1, nameOfCompanies.length - 1))
        val result = stockHelper.takeStock(currentData)

        return result.toUI()
    }
}
