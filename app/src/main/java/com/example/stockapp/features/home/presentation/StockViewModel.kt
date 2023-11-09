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
    private val getSearchStockDataUseCase: GetSearchStockDataUseCase,
) : ViewModel() {

    @Inject
    lateinit var stockAdapter: StockAdapter

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

    fun searchDataStocks(search: String?) {
        searchJob?.cancel()
        var result: MutableList<StockData>? = ArrayList()
        if (search.isNullOrEmpty()) {
            stockAdapter.retrieveStartingData()
        } else {
            searchJob = viewModelScope.launch {
                delay(400)
                result = search.let { getSearchStockDataUseCase.invoke(it) }
                stockAdapter.addStock(result)
                if (result == null) {
                    throw NullPointerException()
                } else {

                }
            }
        }
    }
}
