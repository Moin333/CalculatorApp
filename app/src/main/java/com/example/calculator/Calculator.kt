package com.example.calculator

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculator")
data class Calculator(
    val inputValue: String,
    val calculatedValue: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
