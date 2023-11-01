package com.example.stockapp.features.home.data

import com.example.stockapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerDataApi {

    @GET("symbol/NASDAQ?")
    suspend fun getNameOfStock(@Query("apikey") apikey: String = STOCK_KEY): List<StockNameDTO>

    @GET("profile/{tickerSymbol}")
    suspend fun getAllStock(
        @Path("tickerSymbol") tickerSymbol: String,
        @Query("apikey") apikey: String = STOCK_KEY
    ): MutableList<StockDTO>

    companion object {
        const val BASE_URL = "https://financialmodelingprep.com/api/v3/"
        const val STOCK_KEY = BuildConfig.KEY_STOCKS
    }

}
