package com.example.calculator

data class ExchangeRateResponse(
    val base: String,
    val disclaimer: String,
    val license: String,
    val rates: Map<String, Double>,
    val timestamp: Int
)