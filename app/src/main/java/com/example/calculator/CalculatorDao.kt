package com.example.calculator

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculatorDao {
    @Upsert
    suspend fun addOrUpdate(calculator: Calculator)

    @Query("SELECT * FROM calculator ORDER BY id DESC")
    suspend fun getAll(): Flow<List<Calculator>>

    @Delete
    suspend fun delete(calculator: Calculator)

}