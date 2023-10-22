package com.example.stockapp.features.home.domain

import com.example.stockapp.features.home.data.StockDTO
import com.example.stockapp.features.home.data.StockNameDTO
import com.example.stockapp.features.home.presentation.DataUI

data class DataDomain(
   val stockNameDTOList : List<StockNameDTO>,
   val stockDTOList : MutableList<StockDTO>
) {
    fun toUI() = DataUI(stockNameDTOList,stockDTOList)
}
