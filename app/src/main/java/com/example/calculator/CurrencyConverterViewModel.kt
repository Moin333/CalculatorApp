package com.example.calculator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CurrencyConverterViewModel : ViewModel() {

    private val _rates = MutableStateFlow<Map<String, Double>>(emptyMap())
    val exchangeRates: StateFlow<Map<String, Double>> = _rates

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchExchangeRates(appId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getExchangeRates(appId)
                _rates.value = response.rates
                _error.value = null
            } catch (e: Exception) {
                Log.e("CurrencyConverterViewModel", "Error: ${e.message}")
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun convertCurrency(amount: Double, fromRate: Double, toRate: Double): Double {
        return if (fromRate > 0 && toRate > 0) {
            (amount / fromRate) * toRate
        } else {
            0.0
        }
    }
}
