package com.example.cryptoappk.data.network

import com.example.cryptoappk.data.network.model.CoinInfoJsonContainerDto
import com.example.cryptoappk.data.network.model.CoinNameListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top/totalvolfull")
   suspend fun getTopCoinsInfo(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_LIMIT) limit: Int = 10,
        @Query(QUERY_PARAM_TO_SYMBOL) tSym: String = CURRENCY
    ): CoinNameListDto

    @GET("pricemultifull")
   suspend fun getFullPriceList(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_FROM_SYMBOLS) fSyms: String = CURRENCY,
        @Query(QUERY_PARAM_TO_SYMBOLS) tSyms: String = CURRENCY

    ): CoinInfoJsonContainerDto

    companion object {
        private const val API_KEY = "c6ac240b04d10d024135e0eea6648b65dc43c57fbc93481f89b006b2a3122428"

        private const val QUERY_PARAM_API_KEY = "api_key"
        private const val QUERY_PARAM_LIMIT = "limit"
        private const val QUERY_PARAM_TO_SYMBOL = "tsym"
        private const val QUERY_PARAM_TO_SYMBOLS = "tsyms"
        private const val QUERY_PARAM_FROM_SYMBOLS = "fsyms"

        private const val CURRENCY = "USD"
    }
}