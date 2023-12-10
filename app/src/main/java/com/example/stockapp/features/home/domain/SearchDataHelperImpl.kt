package com.example.stockapp.features.home.domain

class SearchDataHelperImpl:SearchDataHelper {

    override fun takeSearchData(item: String): SearchDataDomain {
        return SearchDataDomain(item)
    }

}
