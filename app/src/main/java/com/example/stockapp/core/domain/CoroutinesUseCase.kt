package com.example.stockapp.core.domain

import com.example.stockapp.features.home.presentation.StockData

interface  CoroutinesUseCase<IN: Any?, OUT: Any?> {
    suspend fun invoke(params: IN): OUT
}
