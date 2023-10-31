package com.example.stockapp.features.home.domain

import android.content.Context
import android.widget.Toast
import com.example.stockapp.core.domain.CoroutinesUseCase
import com.example.stockapp.features.home.data.StockDTO
import com.example.stockapp.features.home.presentation.StockData
import javax.inject.Inject


class GetStockDataUseCase @Inject constructor(
    private val repository: DataRepository,
    private val stockHelper: StockDataHelper
) : CoroutinesUseCase<String, StockData> {
    private var serverDataStock: List<StockDTO> = ArrayList()

    override suspend fun invoke(params: String): StockData {
        val stockNamesAll = repository.getAllNameOfStocks()
        val stockNames = stockNamesAll.subList(LIST_START, LIST_END)

        val currentCompanies = stockNames.map { it.companyName }
        val nameOfCompanies: String = currentCompanies.toString().replace(" ", "")

        val result: DataDomain = if (nameOfCompanies.isEmpty()) {
            stockHelper.takeStock(serverDataStock)
        } else {
            val currentData = repository.getAllDataOfStocks(
                nameOfCompanies.substring(
                    1,
                    nameOfCompanies.length - 1
                )
            )
            serverDataStock = currentData
            stockHelper.takeStock(serverDataStock)
        }

        return result.toUI()
    }

    companion object {
        const val LIST_START = 0
        const val LIST_END = 10
    }
}
