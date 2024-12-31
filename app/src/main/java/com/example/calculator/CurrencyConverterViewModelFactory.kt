package com.example.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CurrencyConverterViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyConverterViewModel::class.java)) {
            return CurrencyConverterViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}