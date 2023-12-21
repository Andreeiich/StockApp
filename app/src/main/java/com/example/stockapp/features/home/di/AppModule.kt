package com.example.stockapp.features.home.di

import android.content.Context
import com.example.stockapp.features.home.data.DataRepositoryImpl
import com.example.stockapp.features.home.data.ServerDataApi
import com.example.stockapp.features.home.domain.StockDataHelper
import com.example.stockapp.features.home.domain.StockDataHelperImpl
import com.example.stockapp.features.home.domain.DataRepository
import com.example.stockapp.features.home.domain.SearchDataHelper
import com.example.stockapp.features.home.domain.SearchDataHelperImpl
import com.example.stockapp.features.home.presentation.StockAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideServerDataApi(): ServerDataApi {
        return Retrofit.Builder()
            .baseUrl(ServerDataApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServerDataApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDataRepository(api: ServerDataApi): DataRepository {
        return DataRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideBusinessLogicObject(): StockDataHelper {
        return StockDataHelperImpl()
    }

    @Provides
    @Singleton
    fun provideStockAdapter(): StockAdapter {
        return StockAdapter()
    }

    @Provides
    @Singleton
    fun provideBusinessLogicSearch(): SearchDataHelper {
        return SearchDataHelperImpl()
    }


}
