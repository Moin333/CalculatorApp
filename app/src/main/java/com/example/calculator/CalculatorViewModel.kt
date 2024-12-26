package com.example.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CalculatorViewModel(private val dao: CalculatorDao) : ViewModel() {
    val history: Flow<List<Calculator>> = dao.getAll()

    fun addOrUpdate(calculator: Calculator) {
        viewModelScope.launch {
            dao.addOrUpdate(calculator)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            dao.clearAll()
        }
    }
}