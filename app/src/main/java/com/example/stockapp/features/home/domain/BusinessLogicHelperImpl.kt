package com.example.stockapp.features.home.domain


class BusinessLogicHelperImpl : BusinessLogicHelper {

    override suspend fun doWork(params: DataDomain): DataDomain {
        return params.copy(title = "qwerty")
    }
}
