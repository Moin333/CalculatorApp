package com.example.calculator

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculatorDao {
    @Insert
    suspend fun addOrUpdate(calculator: Calculator)

    @Query("SELECT * FROM calculator ORDER BY id DESC")
    fun getAll(): Flow<List<Calculator>>

    @Query("DELETE FROM calculator")
    suspend fun clearAll()
}