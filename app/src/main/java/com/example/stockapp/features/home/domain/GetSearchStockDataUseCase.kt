package com.example.stockapp.features.home.domain

import com.example.stockapp.core.domain.CoroutinesUseCase
import com.example.stockapp.features.home.data.StockDTO
import com.example.stockapp.features.home.data.StockNameDTO
import com.example.stockapp.features.home.presentation.StockData
import kotlinx.coroutines.delay
import javax.inject.Inject

class GetSearchStockDataUseCase @Inject constructor(
    private val repository: DataRepository,
    private val stockHelper: StockDataHelper
) : CoroutinesUseCase<String, MutableList<StockData>?> {

    override suspend fun invoke(params: String): MutableList<StockData>? {

        val searchData = repository.getDataOfStockByQuery(params)
        val nameOfCompanies = mappingCompanyName(searchData)

        if (nameOfCompanies.isEmpty()) {
            return null
        }

        val dataForRequest = getStringForRequest(nameOfCompanies)

        val currentData =
            repository.getAllDataOfStocks(dataForRequest)

        return prepareDataForUI(currentData)
    }

    private fun getStringForRequest(nameOfCompanies: String): String {

        return nameOfCompanies.substring(
            1,
            nameOfCompanies.length - 1
        )
    }

    private fun mappingCompanyName(searchData: List<StockNameDTO>): String {
        val companyName = searchData.map { it.companyName }
        val nameOfCompanies = companyName.toString().replace(" ", "")
        return nameOfCompanies
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


}
