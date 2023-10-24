package com.example.stockapp.features.home.domain


interface BusinessLogicHelper {
    suspend fun doWork(params: DataDomain): DataDomain
}
