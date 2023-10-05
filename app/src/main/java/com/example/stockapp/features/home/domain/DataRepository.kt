package com.example.stockapp.features.home.domain

import com.example.stockapp.features.home.data.DataDTO

interface DataRepository {
    suspend fun getData(): DataDTO
}