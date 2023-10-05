package com.example.stockapp.features.home.di

import com.example.stockapp.features.home.data.DataRepositoryImpl
import com.example.stockapp.features.home.data.ServerDataApi
import com.example.stockapp.features.home.domain.BusinessLogicHelper
import com.example.stockapp.features.home.domain.BusinessLogicHelperImpl
import com.example.stockapp.features.home.domain.DataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideServerDataApi(): ServerDataApi {
        return Retrofit.Builder()
            .baseUrl("https://blabla")
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
    fun provideBussnesLogicObject(): BusinessLogicHelper {
        return BusinessLogicHelperImpl()
    }
}