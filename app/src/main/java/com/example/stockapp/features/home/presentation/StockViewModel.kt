package com.example.stockapp.features.home.presentation

import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.stockapp.R
import com.example.stockapp.features.home.domain.GetStockDataUseCase
import com.example.stockapp.features.home.domain.GetSearchStockDataUseCase
import com.example.stockapp.features.home.domain.UserSearchHistoryService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val getStockDataUseCase: GetStockDataUseCase,
    private val getSearchStockDataUseCase: GetSearchStockDataUseCase,
    private val getRequests: UserSearchHistoryService,
) : ViewModel() {
    var searchJob: Job? = null
    var showMoreStocksJob: Job? = null


    private val _state = MutableStateFlow<List<StockData>?>(null)
    private val _search = MutableStateFlow<List<StockData>?>(null)
    private val _requests = MutableStateFlow<List<SearchData>?>(null)
    private val _popularRequests = MutableStateFlow<List<SearchData>?>(null)
    private val _exceptionOfRequest = MutableStateFlow<Throwable?>(null)
    private val _exceptionOfShowMore = MutableStateFlow<Throwable?>(null)
    private val _searchInsideMore = MutableStateFlow<List<StockData>?>(null)
    private val _searchIncludeMore = MutableStateFlow<List<StockData>?>(null)
    val searched = _search
    val state = _state
    val request = _requests
    val popularRequests = _popularRequests
    val exception = _exceptionOfRequest
    val exceptionOfShowMore = _exceptionOfShowMore
    val searchedInside = _searchInsideMore
    val searchIncludeMore = _searchIncludeMore

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

    fun searchDataStocks(search: String) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(secondsOfDelay)
            if (search.isNotEmpty()) {
                val result = search.let { getSearchStockDataUseCase.invoke(it) }
                if (result == null || result.isEmpty()) {
                    exception.value = NullPointerException()
                } else {
                    searchedInside.value = result
                    searched.value = result.take(stocksStartSearch)
                }
                changeListRequestsOfUser(search)
            }
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

    fun showMoreStocks() {

        val size = searchedInside.value?.size
        if (showMoreStocksJob != null) {
            exceptionOfShowMore.value = NullPointerException()
        }
        checkStateExceptionShowMore()
        showMoreStocksJob = viewModelScope.launch {
            if (size?.let { size -> size > stocksStartSearch } == true) {
                exceptionOfShowMore.value = null
                searchIncludeMore.value = searchedInside.value
            }
        }
    }

    fun checkStateExceptionShowMore() {
        showMoreStocksJob = null
    }

    companion object {
        const val secondsOfDelay = 400L
        private const val stocksStartSearch: Int = 5
    }

}
