package com.example.stockapp.features.home.domain

import com.example.stockapp.core.domain.CoroutinesUseCase
import com.example.stockapp.features.home.data.StockDTO
import com.example.stockapp.features.home.presentation.StockData
import javax.inject.Inject


class GetStockDataUseСase @Inject constructor(
    private val repository: DataRepository,
    private val stockHelper: StockDataHelper
) : CoroutinesUseCase<String, StockData> {

    override suspend fun invoke(params: String): StockData {
        val stockNamesAll = repository.getAllNameOfStocks()
        val serverDataStock: MutableList<StockDTO> = ArrayList()
        val currentData: MutableList<StockDTO> = ArrayList()
        val currentCompanies: MutableList<String> = ArrayList()

        val stockNames = stockNamesAll.subList(0,10)
        currentCompanies.addAll(stockNames.map { it.companyName })
        val str: String = currentCompanies.toString().replace(" ", "")
        currentData.addAll(repository.getAllDataOfStocks(str.substring(1,str.length-1)))
        serverDataStock.addAll(currentData)

        val result = stockHelper.takeStock(stockNames, serverDataStock)
        return result.toUI()
    }
}
