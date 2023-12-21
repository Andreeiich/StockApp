package com.example.stockapp.core.domain

interface SearchUseCase<IN : Any?, OUT : Any?> {


    fun getHadRequests(): OUT

    fun getPopularRequests(): OUT

    fun changeListRequestsOfUser(string: IN)
}
