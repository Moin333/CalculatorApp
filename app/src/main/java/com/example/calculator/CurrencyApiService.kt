package com.example.calculator

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("latest.json")
    suspend fun getExchangeRates(
        @Query("app_id") appId: String = BuildConfig.OPEN_API_ACCESS_KEY
    ): ExchangeRateResponse
}