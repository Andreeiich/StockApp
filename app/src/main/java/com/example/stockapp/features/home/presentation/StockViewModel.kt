package com.example.stockapp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockapp.features.home.domain.GetStockDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val getStockDataUseCase: GetStockDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<StockData?>(null)
    val state = _state

    suspend fun getStocks() {
       val result = viewModelScope.launch {
            val result = getStockDataUseCase.invoke("Params")
            state.value = result
        }
        result.join()
        if (state.value?.stocks == null) {
            throw NullPointerException()
        }
    }

    fun setDataInStocksAdapter(data: StockData, adapter: StockAdapter) {
        adapter.apply {
            addStock(data.stocks)
        }
    }

}
