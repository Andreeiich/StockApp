package com.example.stockapp.features.home.domain

import com.example.stockapp.features.home.presentation.SearchData

data class SearchDataDomain(val search: Int) {

    fun toUI() = SearchData(search)

}
