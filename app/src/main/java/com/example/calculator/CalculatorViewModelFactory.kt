package com.example.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CalculatorViewModelFactory(private val dao: CalculatorDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculatorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalculatorViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}