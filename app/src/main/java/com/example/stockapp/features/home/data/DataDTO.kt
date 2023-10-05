package com.example.stockapp.features.home.data

import com.example.stockapp.features.home.domain.DataDomain

data class DataDTO(
    val title: String,
    val subTitle: String,
    val description: String,
    val someDumpInfo: String,
    val importantDataForDomain: String
) {
    fun toDomain() = DataDomain(
        title, subTitle, description, importantDataForDomain
    )
}
