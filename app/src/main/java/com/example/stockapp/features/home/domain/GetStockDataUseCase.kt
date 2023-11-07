package com.example.stockapp.features.home.domain

import com.example.stockapp.core.domain.CoroutinesUseCase
import com.example.stockapp.features.home.data.StockDTO
import com.example.stockapp.features.home.data.StockNameDTO
import com.example.stockapp.features.home.presentation.StockData
import javax.inject.Inject

class GetStockDataUseCase @Inject constructor(
    private val repository: DataRepository,
    private val stockHelper: StockDataHelper
) : CoroutinesUseCase<String, MutableList<StockData>?> {

    override suspend fun invoke(params: String): MutableList<StockData>? {

        val stockNamesAll = getNamesOfStocks()
        val nameOfCompanies = mappingCompanyName(stockNamesAll)

        if (nameOfCompanies.isEmpty() || stockNamesAll.isEmpty()) {
            return null
        }

        val dataForRequest = getStringForRequest(nameOfCompanies)
        val currentData = getAllStocks(dataForRequest)

        return prepareDataForUI(currentData)
    }

    private fun getStringForRequest(nameOfCompanies: String): String {

        return nameOfCompanies.substring(
            1,
            nameOfCompanies.length - 1
        )
    }

    private suspend fun getAllStocks(nameOfCompanies: String): List<StockDTO> {
        val result = repository.getAllDataOfStocks(nameOfCompanies)
        return result
    }

    private fun mappingCompanyName(stockNamesAll: List<StockNameDTO>): String {
        val companyNames = stockNamesAll.map { it.companyName }
        val nameOfCompanies: String = companyNames.toString().replace(" ", "")
        return nameOfCompanies
    }

    private suspend fun getNamesOfStocks(): List<StockNameDTO> {
        val stockNamesAll = repository.getAllNameOfStocks()
        if (stockNamesAll.isNotEmpty()) {
            return stockNamesAll.subList(
                LIST_START,
                LIST_END
            )
        } else {
            return stockNamesAll
        }
    }

    private suspend fun prepareDataForUI(data: List<StockDTO>): MutableList<StockData> {

        var domainItem: DataDomain
        val result: MutableList<StockData> = mutableListOf()
        for (item in data) {
            domainItem = stockHelper.takeStock(item.toDomain())
            result.add(domainItem.toUI())
        }
        return result
    }

    companion object {
        const val LIST_START = 0
        const val LIST_END = 10
    }
}
