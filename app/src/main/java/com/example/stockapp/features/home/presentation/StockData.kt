package com.example.stockapp.features.home.presentation

import com.example.stockapp.features.home.data.StockDTO
import com.example.stockapp.features.home.data.StockNameDTO

data class StockData(
   val stocksName : List<StockNameDTO>,
   val stocks : List<StockDTO>
)
