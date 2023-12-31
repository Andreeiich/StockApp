package com.example.stockapp.features.home.domain

import android.content.Context
import com.example.stockapp.R
import com.example.stockapp.core.domain.SearchUseCase
import com.example.stockapp.features.home.presentation.SearchData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.collections.ArrayList

class UserSearchHistoryService @Inject constructor(
    private val searchHelper: SearchDataHelper,
) :
    SearchUseCase<String, List<SearchData>> {

    private val index = 0
    private val popularRequests
        get() = arrayListOf(
            (R.string.apple),
            R.string.amazon,
            R.string.google,
            R.string.tesla,
            R.string.firstSolar,
            R.string.alibaba,
            R.string.facebook,
            R.string.mastercard
        )

    private var hadRequests = arrayListOf(
        R.string.nokia,
        R.string.intel,
        R.string.visa,
        R.string.amd,
        R.string.gm,
        R.string.nvidia
    )

    override fun getHadRequests(): List<SearchData> {
        return prepareSearchData(hadRequests)
    }

    override fun getPopularRequests(): List<SearchData> {
        return prepareSearchData(popularRequests)
    }

    override fun changeListRequestsOfUser(string: String) {
        hadRequests.add(hadRequests.size, R.string.merc)
        hadRequests.removeAt(index)
    }

    private fun prepareSearchData(list: ArrayList<Int>): MutableList<SearchData> {

        val result: MutableList<SearchData> = mutableListOf()
        var search: SearchDataDomain

        list.map {
            search = searchHelper.takeSearchData(it)
            result.add(search.toUI())
        }
        return result
    }

}
