package com.example.stockapp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockapp.features.home.domain.GetStockDataUseСase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getStockDataUseСase: GetStockDataUseСase
) : ViewModel() {

    private val _state = MutableStateFlow<StockData?>(null)
    val state = _state

    fun getStocks() {
        viewModelScope.launch {
            val result = getStockDataUseСase.invoke("Params")
            state.value = result
        }
    }

    fun getData() {
        getStocks()
    }

    fun setDataInStocksAdapter(data: StockData, adapter: StockAdapter) {
        adapter.apply {
            addStock(data.stocksName, data.stocks)

        }
    }

}
