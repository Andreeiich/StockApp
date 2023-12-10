package com.example.stockapp.features.home.presentation

import android.view.MotionEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockapp.features.home.domain.GetStockDataUseCase
import com.example.stockapp.features.home.domain.GetSearchStockDataUseCase
import com.example.stockapp.features.home.domain.UserSearchHistoryService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val getStockDataUseCase: GetStockDataUseCase,
    private val getSearchStockDataUseCase: GetSearchStockDataUseCase,
    private val getRequests: UserSearchHistoryService,
   ) : ViewModel() {


    private val _state = MutableStateFlow<List<StockData>?>(null)
    private val _search = MutableStateFlow<List<StockData>?>(null)
    private val _requests = MutableStateFlow<List<SearchData>?>(null)
    private val _popularRequests = MutableStateFlow<List<SearchData>?>(null)
    val searched = _search
    val state = _state
    val request = _requests
    val popularRequests = _popularRequests

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

    suspend fun searchDataStocks(search: String) {

        val result = search.let { getSearchStockDataUseCase.invoke(it) }
        if (result == null || result.isEmpty()) {
            searched.value = null
        } else {
            searched.value = result
        }
    }

    fun getRequestsOfUser(): MutableList<SearchData> {
        request.value = getRequests.getHadRequests()
        return request.value as MutableList<SearchData>
    }

    fun getPopularRequests(): MutableList<SearchData> {
        popularRequests.value = getRequests.getPopularRequests()
        return popularRequests.value as MutableList<SearchData>
    }

    fun changeListRequestsOfUser(request: String) {
        getRequests.changeListRequestsOfUser(request)
    }


}
