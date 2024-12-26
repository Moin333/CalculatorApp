package com.example.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CalculatorViewModel(private val dao: CalculatorDao) : ViewModel() {


    init {
        viewModelScope.launch{
            dao.getAll()
        }
    }

    fun addOrUpdate(calculator: Calculator) {
        viewModelScope.launch {
            dao.addOrUpdate(calculator)
        }
    }

    fun delete(calculator: Calculator) {
        viewModelScope.launch {
            dao.delete(calculator)
        }
    }

}