package com.example.stockapp.features.home.domain

import com.example.stockapp.core.domain.CoroutinesUseCase
import com.example.stockapp.features.home.data.StockDTO
import com.example.stockapp.features.home.presentation.DataUI
import javax.inject.Inject


class GetStockDataUsecase @Inject constructor(
    private val repository: DataRepository,
    private val stockHelper: StockDataHelper
) : CoroutinesUseCase<String, DataUI> {

    override suspend fun invoke(params: String): DataUI {
        val serverDataNameOfStocks = repository.getAllNameOfStocks()
        var serverDataStock: MutableList<StockDTO> = ArrayList()
        var currentData: MutableList<StockDTO> = ArrayList()
        var currentCompanies: MutableList<String> = ArrayList()

        for (i in serverDataNameOfStocks.indices) {

            currentCompanies.add(serverDataNameOfStocks.get(i).companyName)

            if (i % 1000 == 0 && i != 0) {

                var str: String = currentCompanies.toString()
                    .substring(1, currentCompanies.toString().length - 1)
                    .replace(" ", "")
                currentCompanies.clear()// to avoid error of http 414
                currentData.addAll(repository.getAllDataOfStocks(str))
            }

            if (i == serverDataNameOfStocks.size - 1) {
                var str: String = currentCompanies.toString()
                    .substring(1, currentCompanies.toString().length - 1)
                    .replace(" ", "")
                currentData.addAll(repository.getAllDataOfStocks(str))
            }
        }
        serverDataStock.addAll(currentData)

        val result = stockHelper.takeStock(serverDataNameOfStocks, serverDataStock)
        return result.toUI()
    }
}
