package com.example.stockapp.features.home.domain

import com.example.stockapp.core.domain.SearchUseCase
import com.example.stockapp.features.home.presentation.SearchData
import javax.inject.Inject

class UserSearchHistoryService @Inject constructor(private val searchHelper: SearchDataHelper) :
    SearchUseCase<String, MutableList<SearchData>> {

    private val popularRequests
        get() = arrayListOf<String>(
            "Apple",
            "Amazon",
            "Google",
            "Tesla",
            "FirstSolar",
            "Alibaba",
            "Facebook",
            "Mastercard"
        )

    private var hadRequests = arrayListOf<String>("Nokia", "intel", "Visa", "AMD", "GM", "Nvidia")
    /* set(value) {
         field = value
         hadRequest.removeAt(5)
         hadRequest.add(field.toString())
     }
     */

    override fun getHadRequests(): MutableList<SearchData> {
        return prepareSearchData(hadRequests)
    }

    override fun getPopularRequests(): MutableList<SearchData> {
        return prepareSearchData(popularRequests)
    }

    override fun changeListRequestsOfUser(string: String) {
        hadRequests.add(index, string)
        hadRequests.removeAt(indexForDelete)
    }

    private fun prepareSearchData(list: List<String>): MutableList<SearchData> {

        val result: MutableList<SearchData> = mutableListOf()
        var search: SearchDataDomain

        for (element in list) {
            search = searchHelper.takeSearchData(element)
            result.add(search.toUI())
        }
        return result
    }


    companion object {
        private var index = 6
        private var indexForDelete = 5
    }
}
