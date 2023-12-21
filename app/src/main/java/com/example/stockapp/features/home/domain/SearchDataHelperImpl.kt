package com.example.stockapp.features.home.domain

class SearchDataHelperImpl : SearchDataHelper {

    override fun takeSearchData(item: Int): SearchDataDomain {
        return SearchDataDomain(item)
    }

}
