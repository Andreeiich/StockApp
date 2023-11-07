package com.example.stockapp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockapp.features.home.domain.GetStockDataUseCase
import com.example.stockapp.features.home.domain.GetSearchStockDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val getStockDataUseCase: GetStockDataUseCase,
    private val getSearchStockDataUseCase: GetSearchStockDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<MutableList<StockData>?>(null)
    val state = _state
    private var searchJob: Job? = null
    suspend fun getStocks() {
        val result = viewModelScope.launch {
            val result = getStockDataUseCase.invoke("Params")
            state.value = result
        }
        result.join()
        if (state.value == null) {
            throw NullPointerException()
        }
    }

    fun setDataInStocksAdapter(data: MutableList<StockData>?, adapter: StockAdapter) {
        adapter.apply {
            addStock(data)
            if (data != null) {
                setStartingData(data)
            }
        }
    }

    fun searchDataStocks(search: String?, adapter: StockAdapter) {
        searchJob?.cancel()

        if (search.isNullOrEmpty()) {
            adapter.retrieveStartingData()
        } else {
            searchJob = viewModelScope.launch {
                delay(400)
                val result = search.let { getSearchStockDataUseCase.invoke(it) }
                result?.let { adapter.addStock(it) }
                if (result == null) {
                    throw NullPointerException()
                }
            }
        }

    }
}
