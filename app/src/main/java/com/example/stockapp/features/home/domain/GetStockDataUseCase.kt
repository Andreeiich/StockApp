package com.example.stockapp.features.home.domain

import com.example.stockapp.core.domain.CoroutinesUseCase
import com.example.stockapp.features.home.data.StockDTO
import com.example.stockapp.features.home.data.StockNameDTO
import com.example.stockapp.features.home.presentation.StockData
import javax.inject.Inject

class GetStockDataUseCase @Inject constructor(
    private val repository: DataRepository,
    private val stockHelper: StockDataHelper
) : CoroutinesUseCase<String, StockData> {

    override suspend fun invoke(params: String): StockData? {
        val stockNamesAll = repository.getAllNameOfStocks()
        val stockNames = if (stockNamesAll.isNotEmpty()) stockNamesAll.subList(
            LIST_START,
            LIST_END
        ) else stockNamesAll

        val companyNames = stockNames.map { it.companyName }
        val nameOfCompanies: String = companyNames.toString().replace(" ", "")

        if(nameOfCompanies.isEmpty() || stockNamesAll.isEmpty()){
            return null
        }
            val currentData = repository.getAllDataOfStocks(
                nameOfCompanies.substring(
                    1,
                    nameOfCompanies.length - 1
                )
            )

            val result = stockHelper.takeStock(currentData)

        return result.toUI()
    }

    companion object {
        const val LIST_START = 0
        const val LIST_END = 10
    }
}
