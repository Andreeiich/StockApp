package com.example.stockapp.features.home.domain

import com.example.stockapp.features.home.presentation.DataUI

data class DataDomain(
    val title: String,
    val subTitle: String,
    val description: String,
    val importantDataForDomain: String
) {
    fun toUI() = DataUI(title, subTitle, description)
}
