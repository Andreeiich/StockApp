package com.example.stockapp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockapp.features.home.domain.GetStockDataUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getStockDataUsecase: GetStockDataUsecase
) : ViewModel() {

    private val _state = MutableStateFlow<DataUI?>(null)
    val state = _state

    fun getStocks() {
        viewModelScope.launch {
            val result = getStockDataUsecase.invoke("Params")
            state.value = result
        }
    }

    fun getData() {
        getStocks()
    }

    fun setDataInStocksAdapter(data: DataUI, adapter: StockAdapter) {
        adapter.apply {
            addStock(data.stocksName, data.stocks)

        }
    }

}
